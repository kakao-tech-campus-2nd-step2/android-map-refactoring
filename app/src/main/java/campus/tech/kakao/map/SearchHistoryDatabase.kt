package campus.tech.kakao.map

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class], version = 1, exportSchema = false)
abstract class SearchHistoryDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        private var Instance: SearchHistoryDatabase? = null

        fun getDatabase(context: Context): SearchHistoryDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, SearchHistoryDatabase::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}