package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.SearchLocationRepository

class SearchLocationUseCase(
    private val searchLocationRepository: SearchLocationRepository
) {
    suspend operator fun invoke(category: String) =
        searchLocationRepository.searchLocation(category)
}