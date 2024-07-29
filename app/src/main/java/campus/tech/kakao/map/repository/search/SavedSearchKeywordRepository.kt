package campus.tech.kakao.map.repository.search

import android.content.Context
import campus.tech.kakao.map.model.search.SearchKeyword
import campus.tech.kakao.map.model.search.SearchKeywordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SavedSearchKeywordRepository @Inject constructor(private val db: SearchKeywordDao) {

    suspend fun saveSearchKeyword(searchKeyword: SearchKeyword) {
        withContext(Dispatchers.IO) {
            db.insert(searchKeyword)
        }
    }

    suspend fun getSavedSearchKeywords(): List<SearchKeyword> {
        return withContext(Dispatchers.IO) {
            db.getAll()
        }
    }

    suspend fun delSavedSearchKeyword(searchKeyword: SearchKeyword) {
        withContext(Dispatchers.IO) {
            db.delete(searchKeyword)
        }
    }
}