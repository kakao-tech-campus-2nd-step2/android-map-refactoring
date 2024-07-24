package ksc.campus.tech.kakao.map.data.apis

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.data.entities.KakaoSearchDTO
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchRetrofitService {
    @GET("/v2/local/search/keyword.json")
    fun requestSearchResultByKeyword(
        @Header("Authorization") restApiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<KakaoSearchDTO>
}

@Module
@InstallIn(SingletonComponent::class)
object SearchKakaoRetrofitService {
    @Provides
    fun provideSearchKakaoRetrofitService(): KakaoSearchRetrofitService {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoSearchRetrofitService::class.java)
    }
}