package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Keyword::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
}