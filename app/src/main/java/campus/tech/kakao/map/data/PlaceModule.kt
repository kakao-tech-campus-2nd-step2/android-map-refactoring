package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlaceModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PlaceDatabase {
        return Room.databaseBuilder(context, PlaceDatabase::class.java, "placeDB").build()
    }

    @Provides
    fun providePlaceDao(database: PlaceDatabase): PlaceDao {
        return database.placeDao()
    }
}