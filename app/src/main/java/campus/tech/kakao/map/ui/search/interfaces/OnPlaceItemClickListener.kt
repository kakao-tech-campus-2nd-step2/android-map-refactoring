package campus.tech.kakao.map.ui.search.interfaces

import campus.tech.kakao.map.domain.model.PlaceDomain

/**
 * 검색 결과 아이템 클릭 이벤트를 처리하는 인터페이스.
 */
interface OnPlaceItemClickListener {
    fun onPlaceItemClicked(place: PlaceDomain)
}
