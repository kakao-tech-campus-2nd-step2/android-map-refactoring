package campus.tech.kakao.map.repository

import campus.tech.kakao.map.database.MapItemDao
import campus.tech.kakao.map.network.KakaoApiService
import campus.tech.kakao.map.model.MapItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface MapRepository {
    suspend fun searchItems(query: String): List<MapItem>
}

class MapRepositoryImpl @Inject constructor(
    private val mapItemDao: MapItemDao,
    private val apiService: KakaoApiService
) : MapRepository {

    override suspend fun searchItems(query: String): List<MapItem> {
        return withContext(Dispatchers.IO) {
            val response = apiService.searchPlaces("KakaoAK your_api_key", query)
            if (response.isSuccessful) {
                response.body()?.documents ?: emptyList()
            } else {
                emptyList()
            }
        }
    }
}