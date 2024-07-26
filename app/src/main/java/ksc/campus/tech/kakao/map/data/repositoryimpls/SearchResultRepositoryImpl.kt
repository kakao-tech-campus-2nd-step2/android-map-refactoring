package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import ksc.campus.tech.kakao.map.data.datasources.SearchResultCachedRemoteDataSource
import ksc.campus.tech.kakao.map.data.mapper.KakaoSearchDtoMapper
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultCachedRemoteDataSource
): SearchResultRepository {
    private val _searchResult = MutableSharedFlow<List<SearchResult>>()

    override val searchResult: SharedFlow<List<SearchResult>>
        get() = _searchResult

    override suspend fun search(text: String, apiKey: String){
        searchResultRemoteDataSource.getSearchResult(text, apiKey, BATCH_COUNT).collectLatest {documents->
            val searchResults = documents.map {
                KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
            }
            _searchResult.emit(searchResults)
        }
    }

    companion object{
        const val BATCH_COUNT = 5
    }
}