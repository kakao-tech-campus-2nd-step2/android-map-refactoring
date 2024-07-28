package campus.tech.kakao.map.repository.keyword

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.database.AppDatabase
import campus.tech.kakao.map.model.Keyword

class KeywordRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "app-database"
    ).build()
    private val keywordDao = db.keywordDao()

    suspend fun update(keyword: String) {
        keywordDao.insert(Keyword(recentKeyword = keyword))
    }

    suspend fun read(): List<String> {
        return keywordDao.getAllKeywords().map { it.recentKeyword }
    }

    suspend fun delete(keyword: String) {
        val keywordEntity = keywordDao.getAllKeywords().find { it.recentKeyword == keyword }
        keywordEntity?.let { keywordDao.delete(it) }
    }
}
