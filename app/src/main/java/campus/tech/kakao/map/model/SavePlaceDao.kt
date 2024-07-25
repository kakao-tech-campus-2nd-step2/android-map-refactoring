package campus.tech.kakao.map.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface SavePlaceDao {
    @Insert
    suspend fun insert(savePlace: SavePlace)

    @Delete
    suspend fun delete(savePlace: SavePlace)

    @Query("SELECT * FROM savePlace")
    suspend fun getAll(): List<SavePlace>

    @Query("SELECT * FROM savePlace WHERE savePlaceName = :name LIMIT 1")
    suspend fun getByName(name: String): SavePlace?
}

