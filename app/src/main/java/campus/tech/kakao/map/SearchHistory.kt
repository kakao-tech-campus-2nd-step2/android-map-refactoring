package campus.tech.kakao.map

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "search_histories")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val searchHistory: String,
    val x: String,
    val y: String
)