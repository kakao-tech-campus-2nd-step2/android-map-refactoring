package campus.tech.kakao.map.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val place: String,
    val address: String,
    val type: String,
    val xPos: String,
    val yPos: String
)

@Entity(tableName = "logs")
data class PlaceLogEntity(
    @PrimaryKey val id: String,
    val place: String
)