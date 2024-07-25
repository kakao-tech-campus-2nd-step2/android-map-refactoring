package campus.tech.kakao.map.data.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY ORDER BY timestamp DESC")
    suspend fun getAllHistoryOrderByTime(): List<History>

    @Query("DELETE FROM HISTORY WHERE location_name = :name")
    suspend fun deleteByName(name: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: History)
}