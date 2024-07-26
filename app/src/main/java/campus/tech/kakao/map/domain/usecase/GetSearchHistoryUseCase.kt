package campus.tech.kakao.map.domain.usecase

interface GetSearchHistoryUseCase {
    suspend operator fun invoke(): List<String>
}