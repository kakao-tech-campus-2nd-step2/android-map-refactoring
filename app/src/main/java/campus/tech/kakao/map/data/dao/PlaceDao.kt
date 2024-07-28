package campus.tech.kakao.map.data.dao

import androidx.room.*
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM places WHERE place LIKE :keyword")
    suspend fun getPlaces(keyword: String): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaces(places: List<PlaceEntity>)

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getPlaceById(id: String): PlaceEntity?

    @Query("DELETE FROM places")
    suspend fun deleteAllPlaces()

    @Query("SELECT * FROM logs")
    suspend fun getLogs(): List<PlaceLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLogs(logs: List<PlaceLogEntity>)

    @Query("DELETE FROM logs WHERE id = :id")
    suspend fun removeLog(id: String)

    @Query("DELETE FROM logs")
    suspend fun deleteAllLogs()
}