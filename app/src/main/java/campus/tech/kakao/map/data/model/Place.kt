package campus.tech.kakao.map.data.model

data class Place(
    val id: String,
    val name: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double,
)
