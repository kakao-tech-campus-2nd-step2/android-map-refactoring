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

    private var query: String = ""
    private var apiKey: String = ""
    private var batchCount: Int = 0
    fun getSearchResult(
    ): Flow<List<Document>> {
        return flow {
            while (true) {
                if (query != _cachedQuery) {
                    emit(listOf())
                    _cache = searchResultRemoteDataSource.getSearchResult(
                        query,
                        apiKey,
                        batchCount
                    )
                    _cachedQuery = query
                    emit(_cache)
                    kotlinx.coroutines.delay(500L)
                }
                else {
                    emit(_cache)
                    kotlinx.coroutines.delay(100L)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun setSearchInfo(query: String, apiKey: String, batchCount: Int) {
        this.query = query
        this.apiKey = apiKey
        this.batchCount = batchCount
    }
}