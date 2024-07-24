package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.model.SearchFromKeywordResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationApi {
    companion object{
        private const val RESULT_SIZE = 15
    }

    private val client = RetrofitInstance.getInstance().create(KakaoAPI::class.java)

    suspend fun getLocations(keyword: String): SearchFromKeywordResponse? {
        return withContext(Dispatchers.IO){
            client.searchFromKeyword(keyword, RESULT_SIZE).body()
        }
    }
}