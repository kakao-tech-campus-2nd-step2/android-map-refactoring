package campus.tech.kakao.map.repository.location

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.database.AppDatabase
import campus.tech.kakao.map.model.Item

class LocationSearcher(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app-database"
    ).build()
    private val itemDao = db.itemDao()

    suspend fun search(keyword: String): List<Item> {
        return itemDao.search("%$keyword%")
    }
}
