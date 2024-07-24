package ksc.campus.tech.kakao.map.repositoriesImpl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import ksc.campus.tech.kakao.map.models.repositories.SearchResult
import ksc.campus.tech.kakao.map.models.repositories.SearchResultRepository
import javax.inject.Inject

class FakeSearchResultRepository @Inject constructor(): SearchResultRepository {
    private val _searchResult: MutableStateFlow<List<SearchResult>> = MutableStateFlow(listOf())

    override val searchResult: Flow<List<SearchResult>>
        get() = _searchResult

    private fun getDummyData(prefix:String):List<SearchResult>{
        val result = mutableListOf<SearchResult>()
        for(i in 0..15) {
            result.add(
                SearchResult(
                    i.toString(),
                    "name $prefix $i",
                    "address $prefix $i",
                    "type $i",
                    0.0,
                    0.0
                )
            )
        }

        return result
    }

    override fun search(text: String, apiKey: String) {
        runBlocking {
            _searchResult.emit(getDummyData(text))
        }
    }
}