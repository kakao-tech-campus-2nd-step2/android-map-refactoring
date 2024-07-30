package campus.tech.kakao.map.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
@Parcelize
data class Location(
    val id: Long,
    val title: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
) : Parcelable {
    companion object {
        fun LocationDto.toLocation(): Location {
            return Location(id.toLong(), title, address, category, x.toDouble(), y.toDouble())

        }
    }
}