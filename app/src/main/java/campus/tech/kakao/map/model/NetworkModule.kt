package campus.tech.kakao.map.model

import android.content.Context
import campus.tech.kakao.map.repository.KakaoApiDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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