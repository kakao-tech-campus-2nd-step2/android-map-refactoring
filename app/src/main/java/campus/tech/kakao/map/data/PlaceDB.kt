package campus.tech.kakao.map.data

import android.content.Context
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.domain.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceDB(private val context: Context) {

    private val placeDao = AppDatabase.getDatabase(context).placeDao()
    private val sharedPreferences = context.getSharedPreferences("LastVisitedPlace", Context.MODE_PRIVATE)

    suspend fun getPlaces(keyword: String): List<Place> {
        val places = KakaoApiClient.getPlaces(keyword)
        updatePlaces(places)
        return places
    }

    suspend fun updatePlaces(places: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllPlaces()
            placeDao.insertPlaces(places.map {
                PlaceEntity(it.id, it.place, it.address, it.category, it.xPos, it.yPos)
            })
        }
    }

    suspend fun getPlaceById(id: String): Place? {
        return withContext(Dispatchers.IO) {
            placeDao.getPlaceById(id)?.let {
                Place(it.id, it.place,it.address, it.type, it.xPos, it.yPos)
            }
        }
    }

    suspend fun updateLogs(logs: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllLogs()
            placeDao.insertLogs(logs.map {
                PlaceLogEntity(it.id, it.place)
            })
        }
    }

    suspend fun removeLog(id: String) {
        withContext(Dispatchers.IO) {
            placeDao.removeLog(id)
        }
    }

    suspend fun getLogs(): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.getLogs().map {
                Place(it.id, it.place, "", "", "", "")
            }
        }
    }

    fun saveLastVisitedPlace(place: Place) {
        val editor = sharedPreferences.edit()
        editor.putString("placeName", place.place)
        editor.putString("roadAddressName", place.address)
        editor.putString("categoryName", place.category)
        editor.putString("yPos", place.yPos)
        editor.putString("xPos", place.xPos)
        editor.apply()
    }

    fun getLastVisitedPlace(): Place? {
        val placeName = sharedPreferences.getString("placeName", null)
        val roadAddressName = sharedPreferences.getString("roadAddressName", null)
        val categoryName = sharedPreferences.getString("categoryName", null)
        val yPos = sharedPreferences.getString("yPos", null)
        val xPos = sharedPreferences.getString("xPos", null)

        return if (placeName != null && roadAddressName != null && categoryName != null && yPos != null && xPos != null) {
            Place("", placeName, roadAddressName, categoryName, xPos, yPos)
        } else {
            null
        }
    }
}
