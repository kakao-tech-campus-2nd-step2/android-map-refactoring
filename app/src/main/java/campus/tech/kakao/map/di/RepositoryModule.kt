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
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(searchQueryDao: SearchQueryDao, visitedPlaceDao: VisitedPlaceDao) : PlaceRepository {
        return PlaceRepositoryImpl(searchQueryDao, visitedPlaceDao)
    }

}
