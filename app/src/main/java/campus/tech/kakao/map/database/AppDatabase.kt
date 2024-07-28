package campus.tech.kakao.map.database

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.model.Keyword
import campus.tech.kakao.map.model.Item
import campus.tech.kakao.map.repository.keyword.KeywordDao
import campus.tech.kakao.map.repository.location.ItemDao

@Database(entities = [Keyword::class, Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
    abstract fun itemDao(): ItemDao
}
