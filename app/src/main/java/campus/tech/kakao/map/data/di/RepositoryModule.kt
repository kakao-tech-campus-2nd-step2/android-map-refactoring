package campus.tech.kakao.map.data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import campus.tech.kakao.map.base.MyApplication
import campus.tech.kakao.map.data.db.PlaceDao
import campus.tech.kakao.map.data.remote.api.KakaoApiService
import campus.tech.kakao.map.repository.LogRepository
import campus.tech.kakao.map.repository.LogRepositoryInterface
import campus.tech.kakao.map.repository.MapRepository
import campus.tech.kakao.map.repository.MapRepositoryInterface
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.PlaceRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Provides
    @Singleton
    fun providePlaceRepository(sharedPreferences: SharedPreferences, kakaoApiService: KakaoApiService): PlaceRepositoryInterface {
        return PlaceRepository(sharedPreferences, kakaoApiService)
    }

    @Provides
    @Singleton
    fun provideLogRepository(context: Context, placeDao: PlaceDao): LogRepositoryInterface {
        return LogRepository(context.applicationContext as MyApplication, placeDao)
    }

    @Provides
    @Singleton
    fun provideMapRepository(context: Context): MapRepositoryInterface {
        return MapRepository(context.applicationContext as MyApplication)
    }
}