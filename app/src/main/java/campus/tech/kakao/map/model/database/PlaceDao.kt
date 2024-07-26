package campus.tech.kakao.map.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.model.data.Place

@Dao
interface PlaceDao {
    @Insert
    suspend fun insert(place: Place)

    @Delete
    suspend fun delete(place: Place)

    @Query("SELECT * FROM places")
    fun getAll(): LiveData<List<Place>>
}
