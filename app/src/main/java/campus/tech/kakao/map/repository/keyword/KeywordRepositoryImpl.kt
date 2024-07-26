package campus.tech.kakao.map.repository.keyword

import android.content.Context

class KeywordRepositoryImpl(private val keywordDao: KeywordDao) : KeywordRepository {

    override fun update(keyword: String) {
        keywordDao.update(keyword)
    }

    override fun read(): List<String> {
        return keywordDao.read()
    }

    override fun delete(keyword: String) {
        keywordDao.delete(keyword)
    }

    override fun close() {
        keywordDao.close()
    }

    companion object {
        fun getInstance(context: Context): KeywordRepositoryImpl {
            return KeywordRepositoryImpl(KeywordDao(context))
        }
    }
}
