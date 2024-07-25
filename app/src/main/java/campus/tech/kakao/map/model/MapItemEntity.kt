package campus.tech.kakao.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mapItems")
data class MapItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
)

data class MapItem(
    val name: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
)

fun MapItem.toEntity(): MapItemEntity {
    return MapItemEntity(
        name = this.name,
        address = this.address,
        category = this.category,
        longitude = this.longitude,
        latitude = this.latitude
    )
}