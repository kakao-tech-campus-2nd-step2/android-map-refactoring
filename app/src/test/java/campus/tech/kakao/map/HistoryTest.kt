package campus.tech.kakao.map

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.source.MapDatabase
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
class HistoryTest {
    lateinit var mapDatabase: MapDatabase
    lateinit var historyRepositoryImpl: HistoryRepositoryImpl

    @Before
    fun init() {
        mapDatabase = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MapDatabase::class.java,
            "map"
        ).build()
        historyRepositoryImpl = HistoryRepositoryImpl(mapDatabase)
    }

    @Test
    fun `검색 기록 저장 테스트`() = runTest {
        val history = mutableListOf<History>()
        for (i in 1..10) {
            val testHist = History(
                "$i",
                "testName$i"
            )
            history.add(testHist)
            historyRepositoryImpl.insertHistory(testHist)
        }
        val savedHistory = historyRepositoryImpl.getAllHistory()
        history.forEachIndexed { index, location ->
            assertEquals(savedHistory[index], location)
        }
    }

    @Test
    fun `ID가 일치하면 같은 Location으로 처리하여 중복으로 저장하지 않음`() = runTest {
        for (i in 1..10) {
            val testHist = History(
                "0",
                "$i"
            )
            historyRepositoryImpl.insertHistory(testHist)
        }
        val savedHistory = historyRepositoryImpl.getAllHistory()
        assertEquals(savedHistory.size, 1)
    }
}