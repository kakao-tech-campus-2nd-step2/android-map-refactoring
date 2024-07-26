package campus.tech.kakao.map.data.remote.api

import campus.tech.kakao.map.data.remote.api.dto.ResultSearchKeyword
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoLocalApi {
    @GET("/v2/local/search/keyword.json")
    fun searchKeyword(
        @Header("Authorization") authorization: String,
        @Query("query") query: String
    ): Call<ResultSearchKeyword>
}