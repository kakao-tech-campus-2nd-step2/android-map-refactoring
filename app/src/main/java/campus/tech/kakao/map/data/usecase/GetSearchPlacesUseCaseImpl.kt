package campus.tech.kakao.map.data.usecase

import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetSearchPlacesUseCase
import javax.inject.Inject

class GetSearchPlacesUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository) :
    GetSearchPlacesUseCase {
    override suspend fun invoke(query: String): List<PlaceVO>? {
        return placeRepository.searchPlaces(query)
    }

}
