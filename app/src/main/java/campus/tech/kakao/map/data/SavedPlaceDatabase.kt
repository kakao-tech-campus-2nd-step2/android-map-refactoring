package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.utilities.PlaceContract
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

