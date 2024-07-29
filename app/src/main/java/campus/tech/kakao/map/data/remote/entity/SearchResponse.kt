package campus.tech.kakao.map.data.remote.entity

data class SearchResponse(
    val meta: MetaEntity,
    val documents: List<DocumentEntity>
)