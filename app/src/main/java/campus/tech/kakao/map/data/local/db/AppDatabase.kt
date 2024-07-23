package campus.tech.kakao.map.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.data.local.entity.SearchQueryEntity
import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity

@Database(entities = [SearchQueryEntity::class, VisitedPlaceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
    abstract fun visitedPlaceDao(): VisitedPlaceDao
}