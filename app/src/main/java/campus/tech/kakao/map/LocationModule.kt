package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.model.datasource.KakaoAPI
import campus.tech.kakao.map.model.datasource.LastLocationlSharedPreferences
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource
import campus.tech.kakao.map.model.datasource.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {

    @Singleton
    @Provides
    fun provideLocationRemoteDataSource(kakaoAPI: KakaoAPI): LocationRemoteDataSource {
        return LocationRemoteDataSource(kakaoAPI)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return SharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLastLocationSharedPreferences(sharedPreferences: SharedPreferences): LastLocationlSharedPreferences {
        return LastLocationlSharedPreferences(sharedPreferences)
    }
}