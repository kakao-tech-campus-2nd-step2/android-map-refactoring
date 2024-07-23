package campus.tech.kakao.map.data

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
    private const val BASE_URL = "https://dapi.kakao.com"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofitLocalCategoryService(retrofit: Retrofit): RetrofitLocalCategoryService {
        return retrofit.create(RetrofitLocalCategoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideRetrofitLocalKeywordService(retrofit: Retrofit): RetrofitLocalKeywordService {
        return retrofit.create(RetrofitLocalKeywordService::class.java)
    }
}