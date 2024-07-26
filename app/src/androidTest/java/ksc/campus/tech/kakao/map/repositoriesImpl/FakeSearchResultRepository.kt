package ksc.campus.tech.kakao.map.repositoriesImpl

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

class FakeSearchResultRepository @Inject constructor(): SearchResultRepository {
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

    private var _searchValue = listOf<SearchResult>()
    override val searchResult: Flow<List<SearchResult>>
        get() = flow {
            while (true){
                emit(_searchValue)
                delay(500)
            }
        }

    override fun search(text: String, apiKey: String){
        _searchValue = getDummyData(text)
    }
}