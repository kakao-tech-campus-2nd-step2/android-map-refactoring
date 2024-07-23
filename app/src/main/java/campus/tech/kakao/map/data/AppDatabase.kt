package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Keyword::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun keywordDao(): KeywordDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "SearchData.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}