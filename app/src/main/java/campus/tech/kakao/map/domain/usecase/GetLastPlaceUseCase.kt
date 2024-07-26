package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.dto.PlaceVO

interface GetLastPlaceUseCase {
    suspend operator fun invoke(): PlaceVO?
}