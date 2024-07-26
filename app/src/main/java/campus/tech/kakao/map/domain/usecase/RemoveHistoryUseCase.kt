package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.HistoryRepository

class RemoveHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(category: String) =
        historyRepository.removeHistory(category)
}