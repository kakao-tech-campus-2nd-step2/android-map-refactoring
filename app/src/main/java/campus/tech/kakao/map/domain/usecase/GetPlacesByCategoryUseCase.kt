package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.data.repository.PlaceRepository
import javax.inject.Inject

class GetPlacesByCategoryUseCase @Inject constructor(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(categoryInput: String, totalPageCount: Int): List<Place> {
        return placeRepository.getPlacesByCategory(categoryInput, totalPageCount)
    }
}
