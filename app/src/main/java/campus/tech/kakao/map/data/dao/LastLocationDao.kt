package campus.tech.kakao.map.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.domain.model.Location

@Dao
interface LastLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastLocation(currLastLocation: Location)
    @Delete
    fun deleteLastLocation(prevLastLocation: Location)
    @Query("SELECT * FROM last_location")
    fun getLastLocation(): Location?
}