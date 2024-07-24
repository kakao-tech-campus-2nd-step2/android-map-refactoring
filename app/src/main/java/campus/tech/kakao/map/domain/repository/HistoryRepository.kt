package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.data.history.History

interface HistoryRepository {
    suspend fun getHistory(): List<History>
    suspend fun addHistory(locationName: String)
    suspend fun removeHistory(locationName: String)
}