package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.model.Location

interface LocationRepository {
    suspend fun saveLocation(location: Location)

    suspend fun loadLocation(): Location
}
