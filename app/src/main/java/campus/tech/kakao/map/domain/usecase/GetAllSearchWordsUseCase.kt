package campus.tech.kakao.map.domain.usecase

import campus.tech.kakao.map.data.model.SavedSearchWord
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import javax.inject.Inject

class GetAllSearchWordsUseCase @Inject constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) {
    suspend operator fun invoke(): List<SavedSearchWord> {
        return savedSearchWordRepository.getAllSearchWords()
    }
}
