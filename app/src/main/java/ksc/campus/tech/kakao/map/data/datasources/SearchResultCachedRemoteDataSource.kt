package ksc.campus.tech.kakao.map.data.datasources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ksc.campus.tech.kakao.map.data.entities.Document
import javax.inject.Inject

class SearchResultCachedRemoteDataSource @Inject constructor(
    private val searchResultRemoteDataSource: SearchResultRemoteDataSource
) {
    private var _cachedQuery: String = ""
    private var _cache: List<Document> = listOf()

    fun getSearchResult(query: String, apiKey: String, batchCount: Int): Flow<List<Document>> {
        return flow {
            if (query != _cachedQuery) {
                // 검색 결과를 불러올 때까지 빈 리스트 반환
                emit(listOf())

                // 서버로부터 데이터를 불러오고 해당 값을 캐시에 저장
                _cache = searchResultRemoteDataSource.getSearchResult(
                    query,
                    apiKey,
                    batchCount
                )
                _cachedQuery = query
            }

            // (갱신된) 캐시 값을 반환
            emit(_cache)
        }.flowOn(Dispatchers.IO)
    }
}