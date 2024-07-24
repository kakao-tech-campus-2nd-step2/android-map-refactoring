package campus.tech.kakao.map.di

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.database.PlaceDatabase
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
    fun provideAppDatabase(@ApplicationContext context: Context): PlaceDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            PlaceDatabase::class.java,
            "place_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlaceDao(placeDatabase: PlaceDatabase): PlaceDao {
        return placeDatabase.placeDao()
    }
}