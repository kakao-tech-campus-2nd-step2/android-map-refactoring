package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDao {
    @Insert
    suspend fun insertPlace(place: Place)

    @Delete
    suspend fun deletePlace(place: Place)

    @Query("SELECT * FROM places")
    suspend fun getAllPlaces() : MutableList<Place>
}