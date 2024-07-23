package campus.tech.kakao.map.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceEntity::class], version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}