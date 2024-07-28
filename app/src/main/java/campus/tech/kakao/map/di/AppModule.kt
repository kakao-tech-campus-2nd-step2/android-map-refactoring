package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.database.AppDatabase
import campus.tech.kakao.map.database.MapItemDao
import campus.tech.kakao.map.repository.MapItemRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabse(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "mapItemDatabase"
        ).build()
    }

    @Provides
    fun provideMapItemDao(db: AppDatabase): MapItemDao {
        return db.mapItemDao()
    }

    @Provides
    @Singleton
    fun provideMapItemRepository(mapItemDao: MapItemDao): MapItemRepository {
        return MapItemRepository(mapItemDao)
    }
}