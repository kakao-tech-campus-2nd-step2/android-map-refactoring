package campus.tech.kakao.map.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY")
    suspend fun getAll(): List<History>

    @Query("DELETE FROM HISTORY WHERE location_name = :name")
    suspend fun deleteByName(name: String)

    @Insert
    suspend fun insertHistory(history: History)
}