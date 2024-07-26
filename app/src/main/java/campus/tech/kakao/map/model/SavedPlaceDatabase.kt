package campus.tech.kakao.map.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import campus.tech.kakao.map.db.PlaceContract
import kotlin.concurrent.Volatile

@Database(entities = [SavedPlace::class], version = PlaceContract.VERSION, exportSchema = false)
abstract class SavedPlaceDatabase : RoomDatabase() {

    abstract fun savedPlaceDao(): SavedPlaceDao

    companion object {
        @Volatile
        private var instance: SavedPlaceDatabase? = null

        fun getInstance(context: Context): SavedPlaceDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, SavedPlaceDatabase::class.java, PlaceContract.DATABASE_NAME
                ).build().also { instance = it }
            }
        }
    }
}

