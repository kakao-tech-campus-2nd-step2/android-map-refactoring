package campus.tech.kakao.map.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.model.MapItemEntity

@Database(entities = [MapItemEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mapItemDao(): MapItemDao

    companion object {
        @Volatile
        private var instanceDb: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instanceDb ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mapItemDatabase"
                ).build()
                instanceDb = instance
                instance
            }
        }
    }
}