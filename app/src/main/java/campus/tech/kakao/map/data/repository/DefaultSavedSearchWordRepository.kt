package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.dao.SavedSearchWordDao
import campus.tech.kakao.map.data.mapper.SavedSearchWordMapper
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.domain.repository.SavedSearchWordRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class DefaultSavedSearchWordRepository
@Inject
constructor(
    private val savedSearchWordDao: SavedSearchWordDao,
) : SavedSearchWordRepository {
    override suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWordDomain) {
        val savedSearchWord = SavedSearchWordMapper.mapToData(searchWord)
        savedSearchWordDao.insertOrUpdateSearchWord(savedSearchWord)
    }

    override suspend fun getAllSearchWords(): List<SavedSearchWordDomain> {
        return savedSearchWordDao.getAllSearchWords()
            .map { SavedSearchWordMapper.mapToDomain(it) }
    }

    override suspend fun deleteSearchWordById(id: Long) {
        savedSearchWordDao.deleteSearchWordById(id)
    }
}
