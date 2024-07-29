package campus.tech.kakao.map.repository.search

import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.search.SearchResults
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

object KakaoAPISetting {
    const val BASE_URL = "https://dapi.kakao.com"
    const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
}

interface KakaoSearchKeywordAPI {
    @GET("/v2/local/search/keyword.json")
    suspend fun getSearchKeyWord(
        @Header("Authorization") key: String,
        @Query("query") keyword: String
    ): SearchResults
}