package campus.tech.kakao.map.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import javax.inject.Singleton

@Singleton
@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM searchHistory ORDER BY searchTime DESC")
    suspend fun getSearchHistory(): List<SearchHistoryData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSearchHistory(vararg searchHistory: SearchHistoryData)

    @Query("DELETE FROM searchHistory WHERE name = :name AND address = :address")
    suspend fun deleteSearchItem(name: String, address: String)

    @Query("UPDATE searchHistory SET searchTime = :newTime WHERE name = :name AND address = :address")
    suspend fun updateSearchTime(name: String, address: String, newTime: Long)

    @Query("SELECT * FROM searchHistory WHERE name = :name AND address = :address")
    suspend fun findSearchItem(name: String, address: String): SearchHistoryData?
}