package campus.tech.kakao.map.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visited_places")
data class VisitedPlaceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val placeName: String,
    val addressName: String,
    val categoryName: String,
    val latitude: Double,
    val longitude: Double,
)
