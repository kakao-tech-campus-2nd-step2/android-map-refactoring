package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.dto.PlaceVO

interface PlaceRepository {
    suspend fun searchPlaces(query: String) : List<PlaceVO>?
    suspend fun saveSearchQuery(place: PlaceVO)
    suspend fun getSearchHistory(): List<String>
    suspend fun removeSearchQuery(query: String)
    suspend fun saveLastPlace(place: PlaceVO)
    suspend fun getLastPlace(): PlaceVO?
}
