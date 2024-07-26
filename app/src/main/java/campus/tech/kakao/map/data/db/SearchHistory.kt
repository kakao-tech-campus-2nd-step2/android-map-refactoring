package campus.tech.kakao.map.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistoryTable")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val placeName: String
)
