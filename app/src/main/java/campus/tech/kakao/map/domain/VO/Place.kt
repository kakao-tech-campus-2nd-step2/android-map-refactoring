package campus.tech.kakao.map.domain.vo

data class Place(
    val id: Int,
    val name: String,
    var address: String? = null,
    var category: PlaceCategory? = null,
    var x : Double,
    var y : Double,
)
