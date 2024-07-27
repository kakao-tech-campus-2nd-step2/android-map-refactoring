package campus.tech.kakao.map.Model

import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("size") size: Int = 15
    ): SearchResult
}