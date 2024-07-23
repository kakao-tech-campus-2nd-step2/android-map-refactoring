package ksc.campus.tech.kakao.map.models.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import ksc.campus.tech.kakao.map.models.datasources.KakaoSearchService
import ksc.campus.tech.kakao.map.models.repositories.SearchResult
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val kakaoSearchService: KakaoSearchService
): SearchResultRepository {
    private var text:String = ""
    private var key:String = ""
    override val searchResult: Flow<List<SearchResult>>
        get() = kakaoSearchService.searchResult(text, key, BATCH_COUNT)

    private fun clearResults() {
        text = ""
    }

    override fun search(text: String, apiKey: String) {
        this.text = text
        this.key = apiKey
    }

    companion object{
        const val BATCH_COUNT = 5
    }
}