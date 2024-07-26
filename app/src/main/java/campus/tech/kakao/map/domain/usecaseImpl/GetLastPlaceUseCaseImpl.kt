package campus.tech.kakao.map.domain.usecaseImpl

import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import javax.inject.Inject

class GetLastPlaceUseCaseImpl @Inject constructor (private val placeRepository: PlaceRepository): GetLastPlaceUseCase {
    override suspend fun invoke(): PlaceVO? {
        return placeRepository.getLastPlace()
    }

}