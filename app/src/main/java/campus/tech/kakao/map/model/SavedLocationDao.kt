package campus.tech.kakao.map.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedLocationDao {
    @Insert
    suspend fun insert(savedLocation: SavedLocation): Long

    @Query("SELECT * FROM savedLocations")
    suspend fun getAll(): List<SavedLocation>

    @Delete
    suspend fun delete(savedLocation: SavedLocation)
}