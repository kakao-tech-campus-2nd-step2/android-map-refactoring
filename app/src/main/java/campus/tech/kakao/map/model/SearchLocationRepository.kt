package campus.tech.kakao.map.model

class SearchLocationRepository(
    private val localSearchService: LocalSearchService
) {
    suspend fun searchLocation(category: String): List<Location> {
        val response = localSearchService.requestLocalSearch(query = category)
        return response.body()?.documents?.map {
            Location(
                name = it.place_name,
                address = it.address_name,
                category = it.category_group_name,
                latitude = it.y.toDouble(),
                longitude = it.x.toDouble()
            )
        } ?: emptyList()
    }
}