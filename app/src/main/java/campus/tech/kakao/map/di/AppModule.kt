package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.database.AppDatabase
import campus.tech.kakao.map.database.MapItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "map_database"
        ).build()
    }

    @Provides
    fun provideMapItemDao(database: AppDatabase): MapItemDao {
        return database.mapItemDao()
    }
}