package campus.tech.kakao.map.repository

import android.database.Cursor
import android.util.Log
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.SavedPlace
import campus.tech.kakao.map.data.SavedPlaceDao
import campus.tech.kakao.map.data.SavedPlaceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class SavedPlaceRepositoryTest {
    private lateinit var savedPlaceRepository: SavedPlaceRepository
    private lateinit var savedPlaceDao : SavedPlaceDao
    private lateinit var cursor: Cursor

    @Before
    fun setup(){
        savedPlaceDao = mockk(relaxed = true)
        savedPlaceRepository = SavedPlaceRepository(savedPlaceDao)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `sqlite에 저장된 장소명들을 잘 불러오는 지 확인`() = runTest{
        coEvery { savedPlaceDao.readSavedPlaceData() }.returnsMany(listOf(SavedPlace("저장된 장소명"), SavedPlace("저장된 장소명")))

        val savedPlaceList = savedPlaceRepository.getAllSavedPlace()
        assert(savedPlaceList == listOf<SavedPlace>(SavedPlace("저장된 장소명"), SavedPlace("저장된 장소명")))

        coVerify { savedPlaceDao.readSavedPlaceData() }

    }

    @Test
    fun `검색어가 이전에 저장되어있지 않은 경우의 데이터 저장`() = runTest{
        val place = Place("새로운 장소", "새로운 위치", "카테고리", "x", "y")
        coEvery { savedPlaceDao.readSavedPlaceDataWithSamedName("새로운 장소") } returns listOf()

        savedPlaceRepository.writePlace(place)

        coVerify { savedPlaceDao.insertSavedPlaceData(place.name) }
        coVerify (inverse = true){ savedPlaceDao.deleteSavedPlace(place.name) }
    }

    @Test
    fun `검색어가 이전에 저장되어있는 경우의 데이터 저장`() = runTest{
        val place = Place("저장된 장소", "저장된 위치", "카테고리", "x", "y")
        coEvery { savedPlaceDao.readSavedPlaceDataWithSamedName("저장된 장소") } returns listOf(SavedPlace(place.name))

        savedPlaceRepository.writePlace(place)

        coVerify { savedPlaceDao.insertSavedPlaceData(place.name) }
        coVerify { savedPlaceDao.deleteSavedPlace(place.name) }
    }

    @Test
    fun `검색어 삭제가 잘 요청되는지`() = runTest{
        val savedPlace = SavedPlace("저장된 장소")
        coEvery { savedPlaceDao.deleteSavedPlace(savedPlace.name) } returns Unit

        savedPlaceRepository.deleteSavedPlace(savedPlace)

        verify { savedPlaceDao.deleteSavedPlace(savedPlace.name) }
    }
}