package campus.tech.kakao.map.data.history

import campus.tech.kakao.map.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {
    override suspend fun getHistory(): List<History> {
        return historyDao.getAll()
    }

    override suspend fun addHistory(locationName: String) {
        historyDao.deleteByName(locationName)
        historyDao.insertHistory(History(name = locationName))
    }

    override suspend fun removeHistory(locationName: String) {
        historyDao.deleteByName(locationName)
    }
}