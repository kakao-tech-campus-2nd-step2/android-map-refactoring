package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.model.SearchFromKeywordResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationRemoteDataSource @Inject constructor(
    private val kakaoAPI: KakaoAPI,
    private val dispatchersIO: CoroutineDispatcher
) {
    companion object{
        private const val RESULT_SIZE = 15
    }

    suspend fun getLocations(keyword: String): SearchFromKeywordResponse? {
        return withContext(dispatchersIO){
            kakaoAPI.searchFromKeyword(keyword, RESULT_SIZE).body()
        }
    }
}