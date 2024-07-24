package campus.tech.kakao.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Profile::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE IF EXISTS Profiles")
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS Profiles (" +
                            "uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "name TEXT NOT NULL, address TEXT NOT NULL, " +
                            "type TEXT NOT NULL, latitude TEXT NOT NULL, " +
                            "longitude TEXT NOT NULL)"
                )
            }
        }
    }
}

