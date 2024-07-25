package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.domain.model.LocationDomain

interface LocationRepository {
    suspend fun saveLocation(location: LocationDomain)

    suspend fun loadLocation(): LocationDomain
}
