package campus.tech.kakao.map.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavePlace::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savePlaceDao(): SavePlaceDao
}

