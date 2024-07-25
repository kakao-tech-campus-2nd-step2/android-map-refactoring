package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.model.KakaoLocalService
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.repository.MapRepositoryImpl
import campus.tech.kakao.map.repository.SearchRepository
import campus.tech.kakao.map.repository.SearchRepositoryImpl
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
    fun provideMapRepository(
        @ApplicationContext context: Context
    ): MapRepository {
        return MapRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(
        @ApplicationContext context: Context,
        retrofit: KakaoLocalService
    ): SearchRepository {
        return SearchRepositoryImpl(context, retrofit)
    }

    @Provides
    @Singleton
    fun provideKakaoLocalService(): KakaoLocalService {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoLocalService::class.java)
    }
}
