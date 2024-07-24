package campus.tech.kakao.map.data.local_search

import android.util.Log
import campus.tech.kakao.map.domain.repository.SearchLocationRepository

class SearchLocationRepositoryImpl(
    private val localSearchService: LocalSearchService
) : SearchLocationRepository {
    override suspend fun searchLocation(category: String): List<Location>? =
        try {
            val response = localSearchService.requestLocalSearch(query = category)
            if (response.isSuccessful) {
                response.body()?.documents?.map {
                    Location(
                        name = it.place_name,
                        address = it.address_name,
                        category = it.category_group_name,
                        latitude = it.y.toDouble(),
                        longitude = it.x.toDouble()
                    )
                }
            } else null
        } catch (e: Exception) {
            Log.e("SearchLocationRepository", "searchLocation: ${e.message}", e)
            null
        }
}