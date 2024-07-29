package campus.tech.kakao.map.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import campus.tech.kakao.map.db.PlaceContract
import kotlin.concurrent.Volatile

@Database(entities = [SavedPlace::class], version = 1, exportSchema = false)
abstract class SavedPlaceDatabase : RoomDatabase() {

    abstract fun savedPlaceDao(): SavedPlaceDao
}

