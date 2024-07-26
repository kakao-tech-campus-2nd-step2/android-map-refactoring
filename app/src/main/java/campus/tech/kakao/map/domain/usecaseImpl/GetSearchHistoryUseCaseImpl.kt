package campus.tech.kakao.map.domain.usecaseImpl

import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.domain.usecase.GetSearchHistoryUseCase
import javax.inject.Inject

class GetSearchHistoryUseCaseImpl @Inject constructor(private val placeRepository: PlaceRepository) :
    GetSearchHistoryUseCase {
    override suspend fun invoke(): List<String> {
        return placeRepository.getSearchHistory()
    }
}