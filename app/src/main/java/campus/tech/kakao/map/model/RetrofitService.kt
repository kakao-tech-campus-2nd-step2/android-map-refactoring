package campus.tech.kakao.map.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitService {
    @GET("v2/local/search/keyword.json")
    suspend fun getPlaces(
        @Header("Authorization") apiKey: String,
        @Query("category_group_code") categoryGroupName: String,
        @Query("query") query: String
    ): PlaceResponse
}
