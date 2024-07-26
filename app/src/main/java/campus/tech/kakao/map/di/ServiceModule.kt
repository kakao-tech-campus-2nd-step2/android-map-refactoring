package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.source.RetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    private val baseUrlAddress = "https://dapi.kakao.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl(baseUrlAddress)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }
}