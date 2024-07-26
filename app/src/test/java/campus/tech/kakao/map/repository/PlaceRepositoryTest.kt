package campus.tech.kakao.map.repository

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import campus.tech.kakao.map.api.KakaoApiDataSource
import campus.tech.kakao.map.data.PlaceRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class PlaceRepositoryTest {

    private lateinit var DataSource: KakaoApiDataSource
    private lateinit var placeRepository : PlaceRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        DataSource = mockk(relaxed = true)
        placeRepository = PlaceRepository(DataSource)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `카카오 데이터소스에서 데이터를 호출하는지`() = runTest{
        val text = "전남대학교 광주"
        val result = placeRepository.getKakaoLocalPlaceData(text)
        coVerify { placeRepository.getKakaoLocalPlaceData(text) }
        assert(result[0].name == "전남대학교 광주캠퍼스")
    }
}