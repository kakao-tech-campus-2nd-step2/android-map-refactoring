package campus.tech.kakao.map.domain.usecase

interface RemoveSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}