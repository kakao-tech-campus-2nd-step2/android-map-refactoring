package campus.tech.kakao.map.repository.search

import android.content.Context
import campus.tech.kakao.map.model.search.SearchKeyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class SavedSearchKeywordRepository(context: Context) {

    private val db = SearchKeywordDB.getInstace(context)!!.searchKeywordDao()

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