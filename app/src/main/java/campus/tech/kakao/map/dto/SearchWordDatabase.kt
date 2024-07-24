package campus.tech.kakao.map.dto

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchWord::class], version = SearchWordContract.DB_VERSION)
abstract class SearchWordDatabase:RoomDatabase() {
	abstract fun searchWordDao(): SearchWordDao
	companion object{
		@Volatile
		private var Instance: SearchWordDatabase? = null
		fun getDatabase(context: Context): SearchWordDatabase{
			// if the Instance is not null, return it, otherwise create a new database instance.
			return Instance ?: synchronized(this) {
				Room.databaseBuilder(context, SearchWordDatabase::class.java, SearchWordContract.DB_NAME)
					.build()
					.also { Instance = it }
			}
		}
	}
}