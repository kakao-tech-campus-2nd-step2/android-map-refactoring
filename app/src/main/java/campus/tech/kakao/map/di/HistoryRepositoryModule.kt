package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.domain.repository.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryRepositoryModule {

    @Binds
    abstract fun bindHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository
}