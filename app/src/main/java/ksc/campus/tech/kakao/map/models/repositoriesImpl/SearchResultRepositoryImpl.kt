package ksc.campus.tech.kakao.map.models.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.KakaoSearchService
import ksc.campus.tech.kakao.map.models.repositories.SearchResult
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val kakaoSearchService: KakaoSearchService
): SearchResultRepository {
    private val _searchResult: MutableLiveData<List<SearchResult>> = MutableLiveData(emptyList())
    override val searchResult: LiveData<List<SearchResult>>
        get() = _searchResult

    private fun clearResults() {
        _searchResult.postValue(listOf())
    }

    override fun search(text: String, apiKey: String) {
        clearResults()
        kakaoSearchService.batchSearchByKeyword(text, apiKey, 10) {
            _searchResult.postValue((_searchResult.value ?: listOf()) + it)
        }
    }
}