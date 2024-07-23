package campus.tech.kakao.map.di

import android.content.Context
import campus.tech.kakao.map.data.history.HistoryDao
import campus.tech.kakao.map.data.history.HistoryRepositoryImpl
import campus.tech.kakao.map.data.last_location.LastLocationRepositoryImpl
import campus.tech.kakao.map.data.local_search.LocalSearchService
import campus.tech.kakao.map.data.local_search.SearchLocationRepositoryImpl
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.SearchLocationRepository
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
object RepositoryModule {
    @Provides
    @Singleton
    fun provideHistoryRepository(historyDao: HistoryDao): HistoryRepository =
        HistoryRepositoryImpl(historyDao)

    @Provides
    @Singleton
    fun provideLastLocationRepository(@ApplicationContext context: Context): LastLocationRepository =
        LastLocationRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideSearchLocationRepository(localSearchService: LocalSearchService): SearchLocationRepository =
        SearchLocationRepositoryImpl(localSearchService)


    @Provides
    @Singleton
    fun provideLocalSearchService(): LocalSearchService =
        Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v2/local/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocalSearchService::class.java)
}