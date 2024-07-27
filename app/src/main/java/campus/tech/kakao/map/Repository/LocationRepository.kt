package campus.tech.kakao.map.Repository

import campus.tech.kakao.map.Model.KakaoLocalApi
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.LocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val kakaoLocalApi: KakaoLocalApi,
    private val locationDao: LocationDao
) {
    suspend fun searchLocations(query: String): List<LocationData> {
        return try {
            val response = kakaoLocalApi.searchPlaces(query)
            response.documents.map { place ->
                LocationData(
                    name = place.place_name,
                    location = place.address_name,
                    category = place.category_group_name,
                    latitude = place.y.toDouble(),
                    longitude = place.x.toDouble()
                )
            }
        } catch (e: Exception) {
            // Log the error and return an empty list
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAllLocations(): List<LocationData> {
        return locationDao.getAllLocations()
    }

    suspend fun insertLocation(location: LocationData) {
        locationDao.insertLocation(location)
    }

    suspend fun deleteLocation(location: LocationData) {
        locationDao.deleteLocation(location)
    }

    suspend fun deleteAllLocations() {
        locationDao.deleteAllLocations()
    }
}