package campus.tech.kakao.map.repository.keyword

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import campus.tech.kakao.map.model.Keyword

@Dao
interface KeywordDao {
    @Insert
    suspend fun insert(keyword: Keyword)

    @Query("SELECT * FROM keyword")
    suspend fun getAllKeywords(): List<Keyword>

    @Delete
    suspend fun delete(keyword: Keyword)
}
