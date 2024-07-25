package campus.tech.kakao.map.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.room.Room
import campus.tech.kakao.map.data.database.AppDatabase
import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.data.repository.LocationSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {

    private val Context.dataStore: DataStore<LocationDomain> by dataStore(
        fileName = "location_data.pb",
        serializer = LocationSerializer,
    )

    @Provides
    @ViewModelScoped
    @LocationDataStore
    fun provideDataStore(
        @ApplicationContext context: Context,
    ): DataStore<LocationDomain> {
        return context.dataStore
    }

    @Provides
    @ViewModelScoped
    @SearchWordDatabase
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "search_words.db").build()
    }

    @Provides
    @ViewModelScoped
    fun provideSavedSearchWordDao(@SearchWordDatabase database: AppDatabase) = database.savedSearchWordDao()

}
