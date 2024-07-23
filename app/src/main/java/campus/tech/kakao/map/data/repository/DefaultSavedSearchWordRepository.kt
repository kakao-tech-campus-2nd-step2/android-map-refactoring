package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.dao.SavedSearchWordDao
import campus.tech.kakao.map.data.model.SavedSearchWord
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultSavedSearchWordRepository
@Inject
constructor(
    private val savedSearchWordDao: SavedSearchWordDao,
) : SavedSearchWordRepository {
    override suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWord) {
        savedSearchWordDao.insertOrUpdateSearchWord(searchWord)
    }

    override suspend fun getAllSearchWords(): List<SavedSearchWord> {
        return savedSearchWordDao.getAllSearchWords()
    }

    override suspend fun deleteSearchWordById(id: Long) {
        savedSearchWordDao.deleteSearchWordById(id)
    }
}
