package campus.tech.kakao.map.domain.usecaseImpl

import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.SaveSearchQueryUseCase
import javax.inject.Inject

class SaveSearchQueryUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository) :
    SaveSearchQueryUseCase {
    override suspend fun invoke(place: PlaceVO) {
        placeRepository.saveSearchQuery(place)
    }

}