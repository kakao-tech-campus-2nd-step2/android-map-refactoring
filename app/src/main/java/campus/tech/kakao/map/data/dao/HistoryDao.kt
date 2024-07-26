package campus.tech.kakao.map.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.domain.model.History

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(newHistory: History)
    @Delete
    fun deleteHistory(oldHistory: History)
    @Query("SELECT * FROM history")
    fun getAllHistory(): List<History>
}