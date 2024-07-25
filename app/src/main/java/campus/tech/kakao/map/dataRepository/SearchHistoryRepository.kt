package campus.tech.kakao.map.dataRepository

import android.content.Context
import android.util.Log
import campus.tech.kakao.map.data.room.SearchHistoryDao
import campus.tech.kakao.map.data.room.SearchHistoryData
import campus.tech.kakao.map.data.room.SearchHistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchHistoryRepository(context: Context) {

    private val searchHistoryDao: SearchHistoryDao =
        SearchHistoryDatabase.getDatabase(context).searchHistoryDao()

    suspend fun getSearchHistory(): List<SearchHistoryData> {
        return withContext(Dispatchers.IO) {
            searchHistoryDao.getSearchHistory()
        }
    }

    suspend fun insertSearchData(name: String, address: String, time: Long) {
        withContext(Dispatchers.IO) {
            val data = mappingData(name, address, time)
            searchHistoryDao.insertSearchHistory(data)
        }
    }

    suspend fun deleteSearchData(name: String, address: String, time: Long) {
        withContext(Dispatchers.IO) {
            Log.d("yeong","Repository: 여기 까지 옴")
            val item = searchHistoryDao.findSearchItem(name, address)
            if (item != null) {
                searchHistoryDao.deleteSearchItem(name, address)
            }
        }
    }

    suspend fun updateTime(name: String, address: String, time: Long) {
        withContext(Dispatchers.IO) {
            searchHistoryDao.updateSearchTime(name, address, time)
        }
    }

    suspend fun findSearchItem(name: String, address: String): SearchHistoryData? {
        return withContext(Dispatchers.IO) {
            searchHistoryDao.findSearchItem(name, address)
        }
    }

    private fun mappingData(name: String, address: String, time: Long): SearchHistoryData {
        return SearchHistoryData(
            name = name,
            address = address,
            searchTime = time
        )
    }
}