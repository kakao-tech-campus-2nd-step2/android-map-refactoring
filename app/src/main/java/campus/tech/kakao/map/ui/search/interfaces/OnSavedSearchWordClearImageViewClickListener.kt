package campus.tech.kakao.map.ui.search.interfaces

import campus.tech.kakao.map.domain.model.SavedSearchWordDomain

/**
 * 저장된 검색어의 clear 버튼 클릭 이벤트를 처리하는 인터페이스.
 */
interface OnSavedSearchWordClearImageViewClickListener {
    fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWordDomain)
}
