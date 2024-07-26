package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface SaveLastPlaceUseCase {
    suspend operator fun invoke(place: PlaceVO)
}