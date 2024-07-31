package campus.tech.kakao.map.data

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.utilities.PlaceContract

@Database(entities = [SavedPlace::class], version = PlaceContract.VERSION, exportSchema = false)
abstract class SavedPlaceDatabase : RoomDatabase() {

    abstract fun savedPlaceDao(): SavedPlaceDao
}

