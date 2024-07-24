package campus.tech.kakao.map.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_words_data")
data class SavedSearchWord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "place_name") val name: String,
    @ColumnInfo(name = "place_id") val placeId: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "latitude") val latitude: Double,
)
