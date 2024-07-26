package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import campus.tech.kakao.map.utilities.Constants
import campus.tech.kakao.map.data.SavedPlaceDao
import campus.tech.kakao.map.data.SavedPlaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): SavedPlaceDatabase {
        return SavedPlaceDatabase.getInstance(context)
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