package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.dto.PlaceVO

interface SaveSearchQueryUseCase {
    suspend operator fun invoke(place: PlaceVO)
}