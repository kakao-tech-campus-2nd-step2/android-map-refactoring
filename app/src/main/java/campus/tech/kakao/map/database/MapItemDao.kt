package campus.tech.kakao.map.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.model.MapItemEntity

@Dao
interface MapItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapItem: MapItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mapItems: List<MapItemEntity>)

    @Query("SELECT * FROM mapItems")
    suspend fun getAllMapItems(): List<MapItemEntity>

    @Query("DELETE FROM mapItems")
    suspend fun deleteAll()

}