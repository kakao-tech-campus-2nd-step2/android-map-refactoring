package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DatabaseName

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @DatabaseName
    fun provideDatabaseName(): String = "kakao_map"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context, @DatabaseName dbName: String): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            dbName
        ).build()
    }

    @Provides
    fun provideSearchQueryDao(database: AppDatabase) = database.searchQueryDao()

    @Provides
    fun provideVisitedPlaceDao(database: AppDatabase) = database.visitedPlaceDao()
}