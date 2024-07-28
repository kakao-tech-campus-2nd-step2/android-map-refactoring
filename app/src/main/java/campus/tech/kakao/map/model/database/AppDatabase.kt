package campus.tech.kakao.map.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch

@Database(entities = [SavedSearch::class, Place::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedSearchDao(): SavedSearchDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
