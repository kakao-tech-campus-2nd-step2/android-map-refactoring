package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.domain.repository.SavedSearchWordRepository
import javax.inject.Inject

class GetAllSearchWordsUseCase @Inject constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) {
    suspend operator fun invoke(): List<SavedSearchWordDomain> {
        return savedSearchWordRepository.getAllSearchWords()
    }
}
