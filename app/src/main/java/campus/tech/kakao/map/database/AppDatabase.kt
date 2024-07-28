package campus.tech.kakao.map.database

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.entity.KeywordEntity
import campus.tech.kakao.map.entity.LocationEntity
import campus.tech.kakao.map.repository.keyword.KeywordDao
import campus.tech.kakao.map.repository.location.ItemDao

@Database(entities = [KeywordEntity::class, LocationEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
    abstract fun itemDao(): ItemDao
}