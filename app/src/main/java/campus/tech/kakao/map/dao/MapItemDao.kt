package campus.tech.kakao.map.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.model.MapItem

@Dao
interface MapItemDao {

    @Insert
    suspend fun insert(mapItem: MapItem)

    @Query("SELECT * FROM MapItem WHERE place_name LIKE :query")
    suspend fun searchItems(query: String): List<MapItem>
}