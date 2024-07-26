package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.AppDatabase
import campus.tech.kakao.map.data.KeywordDao
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
    @DatabaseName
    fun provideDatabaseName(): String {
        return "SearchData.db"
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context,
        @DatabaseName dbName: String
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            dbName
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideKeywordDao(db: AppDatabase): KeywordDao {
        return db.keywordDao()
    }
}
