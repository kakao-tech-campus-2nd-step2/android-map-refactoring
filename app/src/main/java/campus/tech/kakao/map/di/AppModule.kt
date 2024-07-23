package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import campus.tech.kakao.map.data.dao.SavedSearchWordDao
import campus.tech.kakao.map.data.model.Location
import campus.tech.kakao.map.data.network.service.KakaoLocalService
import campus.tech.kakao.map.data.repository.DefaultLocationRepository
import campus.tech.kakao.map.data.repository.DefaultPlaceRepository
import campus.tech.kakao.map.data.repository.DefaultSavedSearchWordRepository
import campus.tech.kakao.map.data.repository.LocationRepository
import campus.tech.kakao.map.data.repository.LocationSerializer
import campus.tech.kakao.map.data.repository.PlaceRepository
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val Context.dataStore: DataStore<Location> by dataStore(
        fileName = "location_data.pb",
        serializer = LocationSerializer,
    )

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Location> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun providePlaceRepository(
        kakaoLocalService: KakaoLocalService,
    ): PlaceRepository {
        return DefaultPlaceRepository(kakaoLocalService)
    }

    @Provides
    @Singleton
    fun provideSavedSearchWordRepository(
        savedSearchWordDao: SavedSearchWordDao,
    ): SavedSearchWordRepository {
        return DefaultSavedSearchWordRepository(savedSearchWordDao)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        dataStore: DataStore<Location>,
    ): LocationRepository {
        return DefaultLocationRepository(dataStore)
    }
}
