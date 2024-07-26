package campus.tech.kakao.map.di

import campus.tech.kakao.map.api.KakaoApiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule{
    @Singleton
    @Provides
    fun provideKakaoApiDataSource(): KakaoApiDataSource {
        return KakaoApiDataSource()
    }
}