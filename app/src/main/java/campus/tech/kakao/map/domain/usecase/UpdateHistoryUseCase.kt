package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.HistoryRepository

class UpdateHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(category: String) =
        historyRepository.addHistory(category)
}