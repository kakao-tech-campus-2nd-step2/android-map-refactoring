package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.Location

interface LastLocationRepository {
    suspend fun insertLastLocation(location: Location)
    suspend fun clearLastLocation()
    suspend fun getLastLocation(): Location?
}