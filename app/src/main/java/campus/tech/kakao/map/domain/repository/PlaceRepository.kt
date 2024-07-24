package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Place

interface PlaceRepository {
    suspend fun getPlaces(placeName: String): List<Place>
    suspend fun updatePlaces(places:List<Place>)
    suspend fun getPlaceById(id: String):Place?
    suspend fun getLogs(): List<Place>
    suspend fun updateLogs(logs: List<Place>)
    suspend fun removeLog(id: String)
}