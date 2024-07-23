package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.database.AppDatabase
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
        return Room.databaseBuilder(context, AppDatabase::class.java, "search_words.db").build()
    }

    @Provides
    fun provideSavedSearchWordDao(database: AppDatabase) = database.savedSearchWordDao()
}
