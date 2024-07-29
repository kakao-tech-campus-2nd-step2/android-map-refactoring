package campus.tech.kakao.map.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.data.dao.SavedSearchWordDao
import campus.tech.kakao.map.data.model.SavedSearchWordData

@Database(entities = [SavedSearchWordData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedSearchWordDao(): SavedSearchWordDao
}
