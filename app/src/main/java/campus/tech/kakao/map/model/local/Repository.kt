package campus.tech.kakao.map.model.local

import campus.tech.kakao.map.repository.local.PlaceEntity


interface Repository {
    suspend fun getPlace(): List<PlaceEntity>
    suspend fun addPlace(locationName: String)
    suspend fun removePlace(locationName: String)
}