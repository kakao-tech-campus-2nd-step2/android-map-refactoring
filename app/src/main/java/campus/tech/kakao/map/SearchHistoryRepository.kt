package campus.tech.kakao.map

import kotlinx.coroutines.flow.Flow

open class SearchHistoryRepository(val dao: SearchHistoryDao) {

    open suspend fun getAllSearchHistories(): ArrayList<SearchHistory> {
        return ArrayList(dao.getAllHistories())
    }
    open suspend fun insert(searchHistory: SearchHistory) {
        dao.insert(searchHistory)
    }

    open suspend fun delete(searchHistory: SearchHistory) {
        dao.delete(searchHistory)
    }

    open suspend fun update(searchHistory: SearchHistory) {
        dao.update(searchHistory)
    }
}