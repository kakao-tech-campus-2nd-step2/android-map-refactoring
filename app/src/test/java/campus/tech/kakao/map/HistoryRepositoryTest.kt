package campus.tech.kakao.map

import campus.tech.kakao.map.data.history.History
import campus.tech.kakao.map.data.history.HistoryDao
import campus.tech.kakao.map.data.history.HistoryRepositoryImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class HistoryRepositoryTest {
    // HistoryDat Mock 객체
    private val mockHistoryDao = mockk<HistoryDao>()

    // SearchLocationRepository 객체
    private val repository = HistoryRepositoryImpl(mockHistoryDao)

    @Test
    fun testGetHistory() = runBlocking {
        // given
        val testHistory = listOf(
            History("test1", 3),
            History("test2", 2),
            History("test3", 1)
        )
        coEvery { mockHistoryDao.getAll() } returns testHistory

        // when
        val result = repository.getHistory()

        // then
        assertEquals(testHistory, result)
    }

    @Test
    fun testAddHistory() = runBlocking {
        // given
        val testHistory = listOf(
            History("testCategory", 2), History("testCategory2", 1)
        )
        coEvery { mockHistoryDao.getAll() } returns testHistory
        coEvery { mockHistoryDao.deleteByName(any()) } just Runs
        coEvery { mockHistoryDao.insertHistory(any()) } just Runs

        // when
        repository.addHistory("testCategory")

        // then
        coVerify { mockHistoryDao.insertHistory(any()) }
    }

    @Test
    fun testRemoveHistory() = runBlocking {
        // given
        coEvery { mockHistoryDao.deleteByName(any()) } just Runs

        // when
        repository.removeHistory("testCategory")

        // then
        coVerify { mockHistoryDao.deleteByName("testCategory") }
    }
}