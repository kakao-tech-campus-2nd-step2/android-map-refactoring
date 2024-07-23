package campus.tech.kakao.map.Model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locations")
    fun getAllLocations(): List<LocationData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(location: LocationData)

    @Delete
    fun deleteLocation(location: LocationData)

    @Query("DELETE FROM locations")
    fun deleteAllLocations()
}