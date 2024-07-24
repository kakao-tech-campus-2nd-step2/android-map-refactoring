package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.model.SavedSearchWord
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import javax.inject.Inject

class InsertOrUpdateSearchWordUseCase @Inject constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) {
    suspend operator fun invoke(savedSearchWord: SavedSearchWord) {
        savedSearchWordRepository.insertOrUpdateSearchWord(savedSearchWord)
    }
}
