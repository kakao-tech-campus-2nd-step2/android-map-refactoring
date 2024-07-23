package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert
    suspend fun insertPlace(place: PlaceEntity)

    @Delete
    suspend fun deletePlace(place: PlaceEntity)

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces() : MutableList<PlaceEntity>
}