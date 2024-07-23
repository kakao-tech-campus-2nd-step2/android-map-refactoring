package campus.tech.kakao.map.data.remote.dto

data class SearchResponse(
    val meta: MetaEntity,
    val documents: List<DocumentEntity>
)