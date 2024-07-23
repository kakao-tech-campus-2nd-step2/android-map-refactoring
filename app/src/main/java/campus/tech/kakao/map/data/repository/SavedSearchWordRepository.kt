package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.model.SavedSearchWord

interface SavedSearchWordRepository {
    suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWord)
    suspend fun getAllSearchWords(): List<SavedSearchWord>
    suspend fun deleteSearchWordById(id: Long)
}
