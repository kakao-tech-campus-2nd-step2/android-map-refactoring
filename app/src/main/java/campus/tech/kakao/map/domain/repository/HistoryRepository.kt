package campus.tech.kakao.map.domain.repository

import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location

interface HistoryRepository {
    suspend fun insertHistory(newHistory: History)
    suspend fun deleteHistory(oldHistory: History)
    suspend fun getAllHistory(): List<History>
}