package campus.tech.kakao.map

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/search/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideRetrofitService(retrofit: Retrofit): RetrofitService {
            return retrofit.create(RetrofitService::class.java)
        }

        @Provides
        @Singleton
        fun provideRetrofitRepository(retrofitService: RetrofitService): RetrofitRepository {
            return RetrofitRepository(retrofitService)
        }
    }

}
