package campus.tech.kakao.map.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Room에서는 autoGenerate를 사용하여 id를 자동으로 생성
    val name: String,
    val address: String,
    val kind: String,
    val latitude: String, // y latitude 위도
    val longitude: String // x longitude 경도
)


fun Place.toLocation(): Location {
    return Location(
        name = this.name,
        address = this.address,
        latitude = this.latitude.toDouble(),
        longitude = this.longitude.toDouble()

    )
}