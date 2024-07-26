package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.SavedSearchWordDomain

interface SavedSearchWordRepository {
    suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWordDomain)

    suspend fun getAllSearchWords(): List<SavedSearchWordDomain>

    suspend fun deleteSearchWordById(id: Long)
}
