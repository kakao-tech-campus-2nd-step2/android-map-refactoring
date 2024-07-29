package campus.tech.kakao.map.model.search

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(
    tableName = "search_keyword",
    indices = [Index(
        value = ["search_keyword"],
        unique = true
    )]
)
data class SearchKeyword(
    @ColumnInfo(name = "search_keyword")
    var searchKeyword: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

@Dao
interface SearchKeywordDao {
    @Query("SELECT * FROM search_keyword")
    fun getAll(): List<SearchKeyword>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(searchKeyword: SearchKeyword)

    @Delete
    fun delete(searchKeyword: SearchKeyword)
}