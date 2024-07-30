package campus.tech.kakao.map.viewmodel

import campus.tech.kakao.map.model.Place

data class UIState(
    val searchResults: List<Place> = emptyList(),
    val savedSearches: List<Place> = emptyList(),
    val noResultsVisible: Boolean = false,
    val searchRecyclerViewVisible: Boolean = false,
    val savedSearchRecyclerViewVisible: Boolean = true
)
