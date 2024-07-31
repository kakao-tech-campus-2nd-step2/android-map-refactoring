package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import campus.tech.kakao.map.utilities.Constants
import campus.tech.kakao.map.data.SavedPlaceDao
import campus.tech.kakao.map.data.SavedPlaceDatabase
import campus.tech.kakao.map.utilities.PlaceContract
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataSourceModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): SavedPlaceDatabase {
        return Room.databaseBuilder(
            context, SavedPlaceDatabase::class.java, PlaceContract.DATABASE_NAME
        ).build()
    }

    @Provides
    fun providePlantDao(savedPlaceDatabase: SavedPlaceDatabase): SavedPlaceDao {
        return savedPlaceDatabase.savedPlaceDao()
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.DataStore.PREFERENCES_NAME)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences>{
        return context.dataStore
    }


}