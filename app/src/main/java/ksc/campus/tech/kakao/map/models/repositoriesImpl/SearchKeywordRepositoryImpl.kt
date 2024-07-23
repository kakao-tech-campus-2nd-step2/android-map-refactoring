package ksc.campus.tech.kakao.map.models.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ksc.campus.tech.kakao.map.models.datasources.SearchKeywordRemoteDataSource
import ksc.campus.tech.kakao.map.models.repositories.SearchKeywordRepository
import javax.inject.Inject

class SearchKeywordRepositoryImpl @Inject constructor(
    private var searchDb: SearchKeywordRemoteDataSource
) : SearchKeywordRepository {
    private val _keywords: MutableLiveData<List<String>> = MutableLiveData(listOf())
    override val keywords: LiveData<List<String>>
        get() = _keywords

    private fun queryKeyWordAndPostValue() {
        val newData = searchDb.queryAllSearchKeywords()
        _keywords.postValue(newData)
    }

    override suspend fun addKeyword(keyword: String) {
        searchDb.insertOrReplaceKeyword(keyword)
        queryKeyWordAndPostValue()
    }

    override suspend fun deleteKeyword(keyword: String) {
        searchDb.deleteKeyword(keyword)
        queryKeyWordAndPostValue()
    }

    override suspend fun getKeywords() {
        val newData = searchDb.queryAllSearchKeywords()
        _keywords.postValue(newData)
    }
}