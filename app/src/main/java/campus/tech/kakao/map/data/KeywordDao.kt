package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface KeywordDao {
    @Query("SELECT * FROM keywords")
    fun getAll(): List<Keyword>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(keyword: Keyword)

    @Query("DELETE FROM keywords WHERE id = :id")
    fun deleteById(id: Int)
}
