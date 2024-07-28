package campus.tech.kakao.map.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.model.data.SavedSearch

@Dao
interface SavedSearchDao {
    @Insert
    suspend fun insert(savedSearch: SavedSearch)

    @Delete
    suspend fun delete(savedSearch: SavedSearch)

    @Query("SELECT * FROM saved_searches")
    fun getAll(): LiveData<List<SavedSearch>>
}
