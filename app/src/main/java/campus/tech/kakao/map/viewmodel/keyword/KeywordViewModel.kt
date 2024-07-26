package campus.tech.kakao.map.viewmodel.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.viewmodel.OnKeywordItemClickListener
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener

class KeywordViewModel(private val keywordRepositoryImpl: KeywordRepository) : ViewModel(),
    OnSearchItemClickListener, OnKeywordItemClickListener {
    private val _keyword = MutableLiveData<List<String>>()
    val keyword: LiveData<List<String>>
        get() = _keyword

    private val _keywordClicked = MutableLiveData<String>()
    val keywordClicked: LiveData<String>
        get() = _keywordClicked

    private fun updateKeywordHistory(keyword: String) {
        keywordRepositoryImpl.delete(keyword)
        keywordRepositoryImpl.update(keyword)
        _keyword.value = keywordRepositoryImpl.read()
    }

    private fun deleteKeywordHistory(keyword: String) {
        keywordRepositoryImpl.delete(keyword)
        _keyword.value = keywordRepositoryImpl.read()
    }

    fun readKeywordHistory() {
        _keyword.value = keywordRepositoryImpl.read()
    }

    fun close() {
        keywordRepositoryImpl.close()
    }

    override fun onSearchItemClick(localUiModel: LocalUiModel) {
        updateKeywordHistory(localUiModel.place)
    }

    override fun onKeywordItemDeleteClick(keyword: String) {
        deleteKeywordHistory(keyword)
    }

    override fun onKeywordItemClick(keyword: String) {
        _keywordClicked.value = keyword
        updateKeywordHistory(keyword)
    }
}
