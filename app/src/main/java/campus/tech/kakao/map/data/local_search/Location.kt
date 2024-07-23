package campus.tech.kakao.map.data.local_search

import java.io.Serializable

data class Location(
    val name: String,
    val address: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
) : Serializable