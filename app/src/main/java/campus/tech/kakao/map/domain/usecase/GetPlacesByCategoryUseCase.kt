package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceDomain
import campus.tech.kakao.map.domain.repository.PlaceRepository
import javax.inject.Inject

class GetPlacesByCategoryUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(categoryInput: String, totalPageCount: Int): List<PlaceDomain> {
        return placeRepository.getPlacesByCategory(categoryInput, totalPageCount)
    }
}
