package campus.tech.kakao.map.kakaoAPI

import campus.tech.kakao.map.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("/v2/local/search/keyword.json")
    fun requsetKakaoMap(
        @Header("Authorization") authorization: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
        @Query("query", encoded = true) query: String = "",
        @Query("page") page: Int = 1
    ): Call<KakaoMapResponse>
}