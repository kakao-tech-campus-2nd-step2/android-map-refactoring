package campus.tech.kakao.map.repository.search

import campus.tech.kakao.map.model.search.Place
import campus.tech.kakao.map.model.search.SearchKeyword
import campus.tech.kakao.map.repository.search.KakaoAPISetting.API_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(private val retrofitKakaoSearchKeyword: KakaoSearchKeywordAPI) {

    suspend fun search(searchKeyword: SearchKeyword): List<Place> {
        if (searchKeyword.searchKeyword == "")
            return emptyList()

        return withContext(Dispatchers.IO) {
            retrofitKakaoSearchKeyword
                .getSearchKeyWord(API_KEY, searchKeyword.searchKeyword)
                .places
        }
    }
}