package campus.tech.kakao.map.Model

import androidx.room.Entity
import com.kakao.vectormap.LatLng

@Entity(tableName = "locations")
data class LocationData(
    val name: String,
    val location: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
) {
    fun getLatLng(): LatLng {
        return LatLng.from(latitude, longitude)
    }
}

