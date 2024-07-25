package campus.tech.kakao.map.data.database

import androidx.room.*
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity

@Database(entities = [PlaceEntity::class, PlaceLogEntity::class], version = 1)
abstract class PlaceDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}