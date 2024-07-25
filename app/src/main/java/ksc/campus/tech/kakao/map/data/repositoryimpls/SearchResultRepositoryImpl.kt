package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ksc.campus.tech.kakao.map.data.datasources.SearchResultCachedRemoteDataSource
import ksc.campus.tech.kakao.map.data.mapper.KakaoSearchDtoMapper
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class SearchResultRepositoryImpl @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultCachedRemoteDataSource
): SearchResultRepository {
    override val searchResult: Flow<List<SearchResult>>
        get() = searchResultRemoteDataSource.getSearchResult().map { documents ->
            documents.map {
                KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
            }
        }

    override fun search(text: String, apiKey: String){
        searchResultRemoteDataSource.setSearchInfo(text, apiKey, BATCH_COUNT)
    }

    companion object{
        const val BATCH_COUNT = 5
    }
}