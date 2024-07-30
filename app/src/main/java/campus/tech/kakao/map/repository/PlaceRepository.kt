package campus.tech.kakao.map.repository

import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.network.KakaoAPIRetrofitService
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.PlaceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepository @Inject constructor(private val apiService: KakaoAPIRetrofitService, private val placeDao: PlaceDao) {

    suspend fun searchPlaces(query: String): List<Document> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getSearchKeyword(query)
                response.documents ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun savePlace(place: Place) {
        withContext(Dispatchers.IO) {
            placeDao.deleteName(place.name)
            placeDao.insert(place)
        }
    }

    suspend fun getSavedPlaces(): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.searchDatabase("%")
        }
    }

    suspend fun deletePlace(name:String, address: String, category: String) {
        withContext(Dispatchers.IO) {
            placeDao.deletePlace(name, address, category)
        }
    }
}