package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase
import campus.tech.kakao.map.domain.usecase.RemoveSearchQueryUseCase
import javax.inject.Inject

class RemoveSearchQueryUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository) :
    RemoveSearchQueryUseCase {
    override fun invoke(query: String) {
        placeRepository.removeSearchQuery(query)
    }

}