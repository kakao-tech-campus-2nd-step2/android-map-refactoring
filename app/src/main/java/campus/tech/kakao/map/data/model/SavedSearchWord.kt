package campus.tech.kakao.map.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_words_data")
data class SavedSearchWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val placeId: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
)
