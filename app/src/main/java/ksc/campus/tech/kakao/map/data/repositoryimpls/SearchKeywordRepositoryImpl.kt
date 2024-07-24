package ksc.campus.tech.kakao.map.data.repositoryimpls

import kotlinx.coroutines.flow.Flow
import ksc.campus.tech.kakao.map.data.datasources.SearchKeywordRemoteDataSource
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import javax.inject.Inject

class SearchKeywordRepositoryImpl @Inject constructor(
    private var searchKeywordDataSource: SearchKeywordRemoteDataSource
) : SearchKeywordRepository {
    override val keywords: Flow<List<String>>
        get() = searchKeywordDataSource.queryAllSearchKeywords()

    override suspend fun addKeyword(keyword: String) {
        searchKeywordDataSource.insertOrReplaceKeyword(keyword)
    }

    override suspend fun deleteKeyword(keyword: String) {
        searchKeywordDataSource.deleteKeyword(keyword)
    }

    override suspend fun getKeywords() {
    }
}