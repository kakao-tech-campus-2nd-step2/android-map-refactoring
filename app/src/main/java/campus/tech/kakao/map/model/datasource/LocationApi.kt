package campus.tech.kakao.map.model.datasource

import android.util.Log
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.Location.Companion.toLocation
import campus.tech.kakao.map.model.LocationDto
import campus.tech.kakao.map.model.SearchFromKeywordResponse
import campus.tech.kakao.map.model.repository.RetrofitInstance
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