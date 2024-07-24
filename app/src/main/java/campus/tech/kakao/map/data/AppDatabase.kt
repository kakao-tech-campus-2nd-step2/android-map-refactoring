package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.*

@Database(entities = [PlaceEntity::class, PlaceLogEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase(){
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "place_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}