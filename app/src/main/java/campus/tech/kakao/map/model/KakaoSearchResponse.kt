package campus.tech.kakao.map.model

import campus.tech.kakao.map.model.MapItemEntity

//MapItem 넘겨받기 -> 매핑
data class KakaoSearchResponse(
    val documents: List<MapItemEntity>
)