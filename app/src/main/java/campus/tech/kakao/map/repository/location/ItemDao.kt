package campus.tech.kakao.map.repository.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import campus.tech.kakao.map.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM location WHERE category LIKE :keyword")
    suspend fun search(keyword: String): List<Item>

    @Insert
    suspend fun insert(item: Item)

    @Delete
    suspend fun delete(item: Item)
}
