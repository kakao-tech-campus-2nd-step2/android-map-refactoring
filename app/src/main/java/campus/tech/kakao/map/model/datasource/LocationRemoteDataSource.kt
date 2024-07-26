package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.model.SearchFromKeywordResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRemoteDataSource @Inject constructor(
    private val kakaoAPI: KakaoAPI
) {
    companion object{
        private const val RESULT_SIZE = 15
    }

    suspend fun getLocations(keyword: String): SearchFromKeywordResponse? {
        return withContext(Dispatchers.IO){
            kakaoAPI.searchFromKeyword(keyword, RESULT_SIZE).body()
        }
    }
}