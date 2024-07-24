package campus.tech.kakao.map.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_results")
data class SearchResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)
