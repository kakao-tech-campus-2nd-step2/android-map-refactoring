package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Location.Companion.toLocation
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.datasource.LocationApi

class LocationRepository(
    private val locationApi: LocationApi
) {
    suspend fun getLocations(query: String): List<Location> {
        val searchFromKeywordResponse = locationApi.getLocations(query)
        val locationDtos: List<LocationDto> = searchFromKeywordResponse?.documents ?: emptyList()
        return toLocations(locationDtos)
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            it.toLocation()
        }
}