package campus.tech.kakao.map.model

class HistoryRepository(
    private val historyDao: HistoryDao
) {
    suspend fun getHistory(): List<String> {
        return historyDao.getAll().map { history ->
            history.name
        }
    }

    suspend fun addHistory(locationName: String) {
        historyDao.deleteByName(locationName)
        historyDao.insertHistory(History(name = locationName))
    }

    suspend fun removeHistory(locationName: String) {
        historyDao.deleteByName(locationName)
    }
}