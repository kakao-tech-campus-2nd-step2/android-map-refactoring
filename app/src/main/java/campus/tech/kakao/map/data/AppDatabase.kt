package campus.tech.kakao.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Profile::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

