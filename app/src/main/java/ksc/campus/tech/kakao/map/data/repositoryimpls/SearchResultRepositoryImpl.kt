package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ksc.campus.tech.kakao.map.data.datasources.KakaoSearchService
import ksc.campus.tech.kakao.map.data.mapper.SearchResponseMapper
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val kakaoSearchService: KakaoSearchService
): SearchResultRepository {
    private var text:String = ""
    private var key:String = ""
    override val searchResult: Flow<List<SearchResult>>
        get() = kakaoSearchService.searchResult(text, key, BATCH_COUNT).map { document ->
            document.map {
                SearchResponseMapper.mapSearchResponseToSearchResult(it)
            }
        }

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