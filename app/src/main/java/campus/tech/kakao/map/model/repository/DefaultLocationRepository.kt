package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Location.Companion.toLocation
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.datasource.LastLocationSharedPreferences
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLocationRepository @Inject constructor(
    private val locationRemoteDataSource: LocationRemoteDataSource,
    private val lastLocationSharedPreferences: LastLocationSharedPreferences
): LocationRepository {
    override suspend fun getLocationAll(query: String): List<Location> {
        val searchFromKeywordResponse = locationRemoteDataSource.getLocations(query)
        val locationDtos: List<LocationDto> = searchFromKeywordResponse?.documents ?: emptyList()
        return toLocations(locationDtos)
    }

    private fun toLocations(locationDtos: List<LocationDto>) =
        locationDtos.map {
            it.toLocation()
        }
    override fun addLastLocation(location: Location){
        lastLocationSharedPreferences.putLastLocation(location)
    }

    override fun getLastLocation(): Location? {
        return lastLocationSharedPreferences.getLastLocation()
    }
}