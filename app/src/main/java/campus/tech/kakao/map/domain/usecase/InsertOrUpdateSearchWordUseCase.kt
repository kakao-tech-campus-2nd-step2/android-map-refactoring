package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import javax.inject.Inject

class InsertOrUpdateSearchWordUseCase @Inject constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) {
    suspend operator fun invoke(savedSearchWord: SavedSearchWordDomain) {
        savedSearchWordRepository.insertOrUpdateSearchWord(savedSearchWord)
    }
}
