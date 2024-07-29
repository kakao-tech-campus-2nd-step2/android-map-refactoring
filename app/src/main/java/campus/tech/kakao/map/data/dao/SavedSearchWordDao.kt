package campus.tech.kakao.map.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import campus.tech.kakao.map.data.model.SavedSearchWordData

@Dao
interface SavedSearchWordDao {
    @Transaction
    suspend fun insertOrUpdateSearchWord(searchWord: SavedSearchWordData) {
        deleteSearchWordByPlaceId(searchWord.placeId)
        insertSearchWord(searchWord)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchWord(searchWord: SavedSearchWordData)

    @Query("DELETE FROM search_words_data WHERE place_id = :placeId")
    suspend fun deleteSearchWordByPlaceId(placeId: String)

    @Query("SELECT * FROM search_words_data")
    suspend fun getAllSearchWords(): List<SavedSearchWordData>

    @Query("DELETE FROM search_words_data WHERE id = :id")
    suspend fun deleteSearchWordById(id: Long)
}
