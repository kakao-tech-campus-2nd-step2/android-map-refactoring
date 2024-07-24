package campus.tech.kakao.map.data.database

import android.content.Context
import androidx.room.*
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity

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