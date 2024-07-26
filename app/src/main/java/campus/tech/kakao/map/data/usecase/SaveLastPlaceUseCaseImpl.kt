package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import javax.inject.Inject

class SaveLastPlaceUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository): SaveLastPlaceUseCase {
    override suspend fun invoke(place: PlaceVO) {
        placeRepository.saveLastPlace(place)
    }

}