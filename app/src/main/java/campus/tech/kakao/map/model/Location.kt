package campus.tech.kakao.map.model

import java.io.Serializable

data class Location(
    val id: Long,
    val title: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double
): Serializable
{
    companion object {
        fun LocationDto.toLocation(): Location {
            return Location(id.toLong(), title, address, category, x.toDouble(), y.toDouble())

        }
    }
}