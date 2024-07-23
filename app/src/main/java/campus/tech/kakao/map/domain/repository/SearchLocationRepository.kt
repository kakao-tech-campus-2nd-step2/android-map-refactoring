package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.data.local_search.Location

interface SearchLocationRepository {
    suspend fun searchLocation(category: String): List<Location>
}