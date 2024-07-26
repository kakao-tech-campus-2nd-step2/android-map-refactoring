package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.PlaceDomain

interface PlaceRepository {
    suspend fun getPlacesByCategory(
        categoryInput: String,
        totalPageCount: Int,
    ): List<PlaceDomain>
}
