package campus.tech.kakao.map.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitLocalCategoryService {
    @GET("/v2/local/search/category.json")
    suspend fun searchPlaceByCategory(
        @Header("Authorization") key: String,
        @Query("category_group_code") categoryGroupCode: String
    ): Response<SearchResult>
}