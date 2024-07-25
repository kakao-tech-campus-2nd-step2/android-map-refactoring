package campus.tech.kakao.map.dto

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchWord::class], version = SearchWordContract.DB_VERSION)
abstract class SearchWordDatabase:RoomDatabase() {
	abstract fun searchWordDao(): SearchWordDao
}