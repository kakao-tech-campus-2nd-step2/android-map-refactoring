package campus.tech.kakao.map.repository.keyword

import campus.tech.kakao.map.entity.KeywordEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeywordRepository @Inject constructor(private val keywordDao: KeywordDao) {

    suspend fun read(): List<String> {
        return keywordDao.getAllKeywords().map { it.keyword }
    }

    suspend fun update(keyword: String) {
        keywordDao.insertKeyword(KeywordEntity(keyword = keyword))
    }

    suspend fun delete(keyword: String) {
        keywordDao.deleteKeyword(KeywordEntity(keyword = keyword))
    }
}