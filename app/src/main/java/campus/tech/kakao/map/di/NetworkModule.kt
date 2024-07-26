package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.KakaoApiClient
import campus.tech.kakao.map.data.KakaoLocalApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @ApiBaseUrl
    fun provideApiBaseUrl(): String {
        return "https://dapi.kakao.com/"
    }

    @Provides
    @Singleton
    fun provideKakaoLocalApiService(@ApiBaseUrl baseUrl: String): KakaoLocalApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(KakaoApiClient.client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalApiService::class.java)
    }
}
