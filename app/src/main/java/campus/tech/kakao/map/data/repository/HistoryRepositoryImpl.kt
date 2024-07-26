package campus.tech.kakao.map.data.repository

import android.content.ContentValues
import android.database.Cursor
import campus.tech.kakao.map.MapContract
import campus.tech.kakao.map.data.source.MapDatabase
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(
    database: MapDatabase
) : HistoryRepository {
    private val historyDao = database.historyDao()

    override suspend fun insertHistory(newHistory: History) {
        withContext(Dispatchers.IO) {
            historyDao.deleteHistory(newHistory)
            historyDao.insertHistory(newHistory)
        }
    }

    override suspend fun deleteHistory(oldHistory: History) {
        withContext(Dispatchers.IO) {
            historyDao.deleteHistory(oldHistory)
        }
    }

    override suspend fun getAllHistory(): List<History> {
        var res: List<History> = listOf()
        withContext(Dispatchers.IO) {
            res = historyDao.getAllHistory()
        }
        return res
    }
}