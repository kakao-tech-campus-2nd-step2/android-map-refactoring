package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ksc.campus.tech.kakao.map.data.datasources.SearchResultRemoteDataSource
import ksc.campus.tech.kakao.map.data.mapper.KakaoSearchDtoMapper
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultRemoteDataSource
): SearchResultRepository {
    private var text:String = ""
    private var key:String = ""
    override val searchResult: Flow<List<SearchResult>>
        get() = searchResultRemoteDataSource.searchResult(text, key, BATCH_COUNT).map { document ->
            document.map {
                KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
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