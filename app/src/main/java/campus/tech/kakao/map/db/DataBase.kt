package campus.tech.kakao.map.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchHistory::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}
