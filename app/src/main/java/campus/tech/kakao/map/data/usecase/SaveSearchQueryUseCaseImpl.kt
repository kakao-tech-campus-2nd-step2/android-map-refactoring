package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.SaveSearchQueryUseCase
import javax.inject.Inject

class SaveSearchQueryUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository) :
    SaveSearchQueryUseCase {
    override fun invoke(place: PlaceVO) {
        placeRepository.saveSearchQuery(place)
    }

}