package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.PlaceVO

interface GetSearchPlacesUseCase {
    suspend operator fun invoke(query: String): List<PlaceVO>?
}