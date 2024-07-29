package campus.tech.kakao.map.model

data class Location(
    val place: String,
    val address: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
)