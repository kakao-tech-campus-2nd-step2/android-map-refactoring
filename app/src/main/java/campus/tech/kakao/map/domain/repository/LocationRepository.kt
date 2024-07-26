package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.LocationDomain

interface LocationRepository {
    suspend fun saveLocation(location: LocationDomain)

    suspend fun loadLocation(): LocationDomain
}
