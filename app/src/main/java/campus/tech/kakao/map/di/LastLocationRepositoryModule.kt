package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repository.LastLocationRepositoryImpl
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LastLocationRepositoryModule {
    @Binds
    abstract fun bindLastLocationRepository(
        lastLocationRepositoryImpl: LastLocationRepositoryImpl
    ): LastLocationRepository
}