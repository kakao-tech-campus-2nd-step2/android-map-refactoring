package campus.tech.kakao.map.domain.model

data class SavedSearchWordDomain(
    val id: Long = 0,
    val name: String,
    val placeId: String,
    val address: String,
    val longitude: Double,
    val latitude: Double,
)
