package campus.tech.kakao.map.data.remote.api.dto
data class ResultSearchKeyword(
    val documents: List<Place>,
    val meta: PlaceMeta
)
