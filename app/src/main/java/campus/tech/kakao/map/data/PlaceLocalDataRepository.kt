package campus.tech.kakao.map.data

import android.content.Context
import campus.tech.kakao.map.data.database.AppDatabase
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class PlaceLocalDataRepository(private val context: Context) : PlaceRepository {

    private val placeDao = AppDatabase.getDatabase(context).placeDao()
    private val lastVisitedPlaceManager = LastVisitedPlaceManager(context)

    override suspend fun getPlaces(placeName: String): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.getPlaces(placeName).map {
                Place(it.id, it.place, it.address, it.type, it.xPos, it.yPos)
            }
        }
    }

    override suspend fun updatePlaces(places: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllPlaces()
            placeDao.insertPlaces(places.map {
                PlaceEntity(it.id, it.place, it.address, it.category, it.xPos, it.yPos)
            })
        }
    }

    override suspend fun getPlaceById(id: String): Place? {
        return withContext(Dispatchers.IO) {
            placeDao.getPlaceById(id)?.let {
                Place(it.id, it.place, it.address, it.type, it.xPos, it.yPos)
            }
        }
    }

    override suspend fun updateLogs(logs: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllLogs()
            placeDao.insertLogs(logs.map {
                PlaceLogEntity(it.id, it.place)
            })
        }
    }

    override suspend fun removeLog(id: String) {
        withContext(Dispatchers.IO) {
            placeDao.removeLog(id)
        }
    }

    override suspend fun getLogs(): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.getLogs().map {
                Place(it.id, it.place, "", "", "", "")
            }
        }
    }

    override suspend fun saveLastVisitedPlace(place: Place) {
        lastVisitedPlaceManager.saveLastVisitedPlace(place)
    }

    override suspend fun getLastVisitedPlace(): Place? {
        return lastVisitedPlaceManager.getLastVisitedPlace()
    }
}
