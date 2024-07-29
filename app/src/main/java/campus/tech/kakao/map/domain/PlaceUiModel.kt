package campus.tech.kakao.map.domain

data class PlaceUiModel(
    val placeList: List<Place>,
    val searchList: List<Place>,
    val isPlaceListVisible: Boolean,
    val isSearchListVisible: Boolean
)
