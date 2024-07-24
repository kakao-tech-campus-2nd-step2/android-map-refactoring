package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.ResultRepositoryImpl
import campus.tech.kakao.map.domain.repository.ResultRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResultRepositoryModule {

    @Binds
    abstract fun bindResultRepository(
        resultRepositoryImpl: ResultRepositoryImpl
    ): ResultRepository
}