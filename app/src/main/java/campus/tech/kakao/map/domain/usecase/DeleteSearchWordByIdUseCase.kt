package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import javax.inject.Inject

class DeleteSearchWordByIdUseCase @Inject constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) {
    suspend operator fun invoke(id: Long) {
        savedSearchWordRepository.deleteSearchWordById(id)
    }
}
