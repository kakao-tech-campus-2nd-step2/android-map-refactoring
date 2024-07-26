package campus.tech.kakao.map.di

import campus.tech.kakao.map.data.local.db.SearchQueryDao
import campus.tech.kakao.map.data.local.db.VisitedPlaceDao
import campus.tech.kakao.map.data.repository.PlaceRepositoryImpl
import campus.tech.kakao.map.domain.repository.PlaceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(impl: PlaceRepositoryImpl): PlaceRepository

}
