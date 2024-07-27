package campus.tech.kakao.map.data.di

import android.content.Context
import campus.tech.kakao.map.data.db.AppDatabase
import campus.tech.kakao.map.data.db.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun providePlaceDao(database: AppDatabase): PlaceDao {
        return database.placeDao()
    }
}