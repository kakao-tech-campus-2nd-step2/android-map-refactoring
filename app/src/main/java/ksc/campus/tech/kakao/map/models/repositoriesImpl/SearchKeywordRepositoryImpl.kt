package ksc.campus.tech.kakao.map.models.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ksc.campus.tech.kakao.map.models.datasources.SearchKeywordRemoteDataSource
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
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