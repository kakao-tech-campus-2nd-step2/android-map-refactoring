package campus.tech.kakao.map

import android.content.Context
import campus.tech.kakao.map.model.datasource.KakaoAPI
import campus.tech.kakao.map.model.datasource.LastLocationSharedPreferences
import campus.tech.kakao.map.model.datasource.LocationRemoteDataSource
import campus.tech.kakao.map.model.datasource.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Singleton
    @Provides
    fun provideLocationRemoteDataSource(kakaoAPI: KakaoAPI): LocationRemoteDataSource {
        return LocationRemoteDataSource(kakaoAPI, Dispatchers.IO)
    }

    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return SharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLastLocationSharedPreferences(sharedPreferences: SharedPreferences): LastLocationSharedPreferences {
        return LastLocationSharedPreferences(sharedPreferences)
    }
}