package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.repositoryImpl.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: PlaceRepositoryImpl): PlaceRepository

}
