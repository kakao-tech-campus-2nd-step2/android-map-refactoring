package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideSearchHistoryDatabase(appContext: Context): SearchHistoryDatabase {
        return Room.databaseBuilder(
            appContext,
            SearchHistoryDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideSearchHistoryDao(database: SearchHistoryDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}