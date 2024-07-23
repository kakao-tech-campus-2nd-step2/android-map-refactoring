package campus.tech.kakao.map.domain.repository

interface HistoryRepository {
    suspend fun getHistory(): List<String>
    suspend fun addHistory(locationName: String)
    suspend fun removeHistory(locationName: String)
}