package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.repository.HistoryRepository

class GetHistoryUseCase(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke() =
        historyRepository.getHistory()
}