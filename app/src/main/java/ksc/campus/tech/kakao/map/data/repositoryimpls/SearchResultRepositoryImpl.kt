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

    override fun search(text: String, apiKey: String): Flow<List<SearchResult>> {
        return searchResultRemoteDataSource.getSearchResult(text, apiKey, BATCH_COUNT).map { document ->
            document.map {
                KakaoSearchDtoMapper.mapSearchResponseToSearchResult(it)
            }
        }
    }

    companion object{
        const val BATCH_COUNT = 5
    }
}