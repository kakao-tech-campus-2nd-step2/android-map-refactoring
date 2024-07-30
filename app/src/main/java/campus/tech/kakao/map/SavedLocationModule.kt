package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.model.datasource.SavedLocationDao
import campus.tech.kakao.map.model.datasource.SavedLocationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedLocationModule {
    @Singleton
    @Provides
    fun provideSavedLocationDatabase(@ApplicationContext context: Context): SavedLocationDatabase {
        return Room.databaseBuilder(
            context,
            SavedLocationDatabase::class.java,
            SavedLocationDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideSavedLocationDao(savedLocationDatabase: SavedLocationDatabase): SavedLocationDao {
        return savedLocationDatabase.savedLocationDao()
    }
}