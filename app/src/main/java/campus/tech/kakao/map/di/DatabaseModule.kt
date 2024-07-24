package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DatabaseModule {
    @Provides
    @ViewModelScoped
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "search_words.db").build()
    }

    @Provides
    @ViewModelScoped
    fun provideSavedSearchWordDao(database: AppDatabase) = database.savedSearchWordDao()
}
