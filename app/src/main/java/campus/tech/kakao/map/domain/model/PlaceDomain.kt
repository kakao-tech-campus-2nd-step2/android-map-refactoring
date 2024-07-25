package campus.tech.kakao.map.domain.model

data class PlaceDomain(
    val id: String,
    val name: String,
    val address: String,
    val category: String,
    val longitude: Double,
    val latitude: Double,
)
