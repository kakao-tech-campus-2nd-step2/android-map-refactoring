package campus.tech.kakao.map.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitLocalKeywordService {
    @GET("/v2/local/search/keyword.json")
    suspend fun searchPlaceByKeyword(
        @Header("Authorization") key: String,
        @Query("query") query: String
    ): Response<SearchResult>
}