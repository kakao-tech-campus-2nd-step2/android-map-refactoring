package campus.tech.kakao.di

import android.content.Context
import campus.tech.kakao.map.api.KakaoLocalApi
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import campus.tech.kakao.map.repository.location.LocationSearcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideKakaoLocalApi(retrofit: Retrofit): KakaoLocalApi {
        return retrofit.create(KakaoLocalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKeywordRepository(@ApplicationContext context: Context): KeywordRepository {
        return KeywordRepository(context)
    }

    @Provides
    @Singleton
    fun provideLocationSearcher(@ApplicationContext context: Context): LocationSearcher {
        return LocationSearcher(context)
    }
}
