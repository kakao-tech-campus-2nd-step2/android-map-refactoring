package campus.tech.kakao.map.data.datasource.Remote

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.datasource.Remote.Response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService{
    @GET(URL)
    fun requestProducts(
        @Header("authorization") auth: String = KEY,
        @Query("query",encoded = true) query: String = "",
        @Query("page") page: Int = CURRENT_PAGE
    ): Call<SearchResponse>

    companion object {
        const val BASE = BuildConfig.BASE_URL
        private const val KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        private const val URL = "/v2/local/search/keyword.json"
        private const val CURRENT_PAGE = 1
    }
}