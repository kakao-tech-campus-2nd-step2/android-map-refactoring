package campus.tech.kakao.map.di

import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.SearchLocationRepository
import campus.tech.kakao.map.domain.usecase.GetHistoryUseCase
import campus.tech.kakao.map.domain.usecase.LoadLastLocationUseCase
import campus.tech.kakao.map.domain.usecase.RemoveHistoryUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastLocationUseCase
import campus.tech.kakao.map.domain.usecase.SearchLocationUseCase
import campus.tech.kakao.map.domain.usecase.UpdateHistoryUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetHistoryUseCase(historyRepository: HistoryRepository) =
        GetHistoryUseCase(historyRepository)

    @Provides
    @Singleton
    fun provideUpdateHistoryUseCase(historyRepository: HistoryRepository) =
        UpdateHistoryUseCase(historyRepository)

    @Provides
    @Singleton
    fun provideRemoveHistoryUseCase(historyRepository: HistoryRepository) =
        RemoveHistoryUseCase(historyRepository)

    @Provides
    @Singleton
    fun provideSaveLastLocationUseCase(lastLocationRepository: LastLocationRepository) =
        SaveLastLocationUseCase(lastLocationRepository)

    @Provides
    @Singleton
    fun provideLoadLastLocationUseCase(lastLocationRepository: LastLocationRepository) =
        LoadLastLocationUseCase(lastLocationRepository)

    @Provides
    @Singleton
    fun provideSearchLocationUseCase(searchLocationRepository: SearchLocationRepository) =
        SearchLocationUseCase(searchLocationRepository)
}