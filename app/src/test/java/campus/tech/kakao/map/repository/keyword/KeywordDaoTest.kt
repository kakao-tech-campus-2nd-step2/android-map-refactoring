package campus.tech.kakao.map.repository.keyword

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.repository.keyword.KeywordDao
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class KeywordDaoTest {

    private lateinit var keywordDao: KeywordDao
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        keywordDao = KeywordDao(context)
    }

    @Test
    fun `카페 키워드가 저장되어 있으면 저장된 키워드를 불러올 수 있다`() {
        // given
        val keyword = "카페"
        keywordDao.update(keyword)

        // when
        val keywords = keywordDao.read()

        // then
        assertEquals(listOf(keyword), keywords)
    }

    @Test
    fun `저장된 키워드인 약국을 삭제한다`() {
        // given
        val keyword = "약국"
        keywordDao.update(keyword)

        // when
        keywordDao.delete(keyword)

        // then
        val keywords = keywordDao.read()
        assertEquals(emptyList<String>(), keywords)
    }
}
