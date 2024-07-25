package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.model.datasource.LastLocationlSharedPreferences
import campus.tech.kakao.map.model.datasource.LocationApi
import campus.tech.kakao.map.model.datasource.SavedLocationDao
import campus.tech.kakao.map.model.datasource.SavedLocationDatabase
import campus.tech.kakao.map.model.repository.DefaultLocationRepository
import campus.tech.kakao.map.model.repository.DefaultSavedLocationRepository
import campus.tech.kakao.map.model.repository.LocationRepository
import campus.tech.kakao.map.model.repository.SavedLocationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationModule {
    @Singleton
    @Provides
    fun provideLocationApi(): LocationApi {
        return LocationApi()
    }

    @Singleton
    @Provides
    fun provideLastLocationlSharedPreferences(): LastLocationlSharedPreferences {
        return LastLocationlSharedPreferences()
    }
}