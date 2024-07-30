package campus.tech.kakao.map.data.room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSearchHistoryDatabase(@ApplicationContext context: Context): SearchHistoryDatabase {
        return SearchHistoryDatabase.getDatabase(context)
    }

    @Provides
    fun provideSearchHistoryDao(database: SearchHistoryDatabase): SearchHistoryDao {
        return database.searchHistoryDao()
    }
}