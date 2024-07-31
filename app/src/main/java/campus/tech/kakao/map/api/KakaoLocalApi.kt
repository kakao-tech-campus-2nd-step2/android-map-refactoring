package campus.tech.kakao.map.api

import campus.tech.kakao.map.data.ResultSearch
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("v2/local/search/keyword.json")
    suspend fun getPlaceData(
        @Query("query") query: String
    ): ResultSearch

}