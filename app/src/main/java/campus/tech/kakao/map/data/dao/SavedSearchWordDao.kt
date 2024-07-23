package campus.tech.kakao.map.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.data.model.SavedSearchWord

@Dao
interface SavedSearchWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWord)

    @Query("SELECT * FROM search_words_data")
    suspend fun getAllSearchWords(): List<SavedSearchWord>

    @Query("DELETE FROM search_words_data WHERE id = :id")
    suspend fun deleteSearchWordById(id: Long)
}
