package campus.tech.kakao.map.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SelectMapItemDao {
    @Query("SELECT * FROM selectMapItems order by id desc")
    fun getAll(): List<MapItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(mapItem: MapItem)

    @Query("DELETE FROM selectMapItems WHERE kakaoId = :id")
    fun deleteItem(id: String)

    @Query("SELECT count(*) FROM selectMapItems WHERE kakaoId = :id")
    fun checkItemInDB(id: String): Int
}