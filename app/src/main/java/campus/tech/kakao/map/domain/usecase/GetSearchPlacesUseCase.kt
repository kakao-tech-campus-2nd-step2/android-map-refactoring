package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.dto.PlaceVO

interface GetSearchPlacesUseCase {
    suspend operator fun invoke(query: String): List<PlaceVO>?
}