package campus.tech.kakao.map.viewmodel

import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.viewmodel.keyword.KeywordViewModel
import campus.tech.kakao.map.viewmodel.search.SearchViewModel

class DualViewModelClickListener(
    private val keywordViewModel: KeywordViewModel,
    private val searchViewModel: SearchViewModel
) : OnSearchItemClickListener {
    override fun onSearchItemClick(localUiModel: LocalUiModel) {
        keywordViewModel.onSearchItemClick(localUiModel)
        searchViewModel.onSearchItemClick(localUiModel)
    }
}
