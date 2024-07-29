package campus.tech.kakao.map.network

import campus.tech.kakao.map.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoAPIRetrofitService {
    @GET("v2/local/search/keyword.json")

    suspend fun getSearchKeyword(
        @Query("query") query: String
    ): SearchResponse
}