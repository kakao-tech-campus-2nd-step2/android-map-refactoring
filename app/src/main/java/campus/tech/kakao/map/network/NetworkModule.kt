package campus.tech.kakao.map.network

import android.content.Context
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
object NetworkModule {
    @Provides
    @Singleton
    fun singletonRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitService::class.java)
    }

    @Provides
    @Singleton
    fun singletonNetwork(retrofitService: RetrofitService): Network{
        return Network(retrofitService)
    }

    @Provides
    @Singleton
    fun singletonSearch(network: Network, @ApplicationContext context: Context): SearchService {
        return SearchService(network, context)
    }
}