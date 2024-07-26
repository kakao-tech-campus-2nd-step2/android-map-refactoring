package campus.tech.kakao.map.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.db.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insertSearchHistory(searchHistory: SearchHistory): Long

    @Query("DELETE FROM SearchHistoryTable WHERE id = :id")
    suspend fun deleteSearchHistoryById(id: Int): Int

    @Query("SELECT * FROM SearchHistoryTable ORDER BY id DESC")
    suspend fun getAllSearchHistory(): MutableList<SearchHistory>
}
