package campus.tech.kakao.map.data.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.local.entity.SearchQueryEntity

@Dao
interface SearchQueryDao {

    @Query("SELECT * FROM search_queries")
    fun getAll(): List<SearchQueryEntity>

    @Delete
    fun delete(searchQueryEntity: SearchQueryEntity)

    @Insert
    fun insert(searchQueryEntity: SearchQueryEntity)
}