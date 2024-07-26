package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.dto.PlaceVO

interface SaveLastPlaceUseCase {
    suspend operator fun invoke(place: PlaceVO)
}