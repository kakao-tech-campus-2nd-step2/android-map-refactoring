package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.remote.network.HttpService
import campus.tech.kakao.map.utils.ApiKeyProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    // 데이터베이스와 네트워크 모듈은 전역적으로 사용할 인스턴스 object 로 사용
    // Usecase와 Repo는 구현체와 인터페이스를 바인딩하기 위해 abstract class로 사용
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "https://dapi.kakao.com/"
    }

    @Provides
    @Singleton
    fun provideApiKeyProvider(): ApiKeyProvider = ApiKeyProvider

    @Provides
    @Singleton
    fun provideHttpService(
        apiKeyProvider: ApiKeyProvider,
        baseUrl: String
    ): HttpService {
        return HttpService(apiKeyProvider, baseUrl)
    }
}