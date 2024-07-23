package campus.tech.kakao.map.ui.interfaces

import campus.tech.kakao.map.data.model.SavedSearchWord

/**
 * 저장된 검색어의 텍스트 뷰 클릭 이벤트를 처리하는 인터페이스.
 */
interface OnSavedSearchWordTextViewClickListener {
    fun onSavedSearchWordTextViewClicked(savedSearchWord: SavedSearchWord)
}
