package campus.tech.kakao.map.ui.search

import android.util.Log
import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.domain.usecase.GetPlacesByCategoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlaceViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getPlacesByCategoryUseCase: GetPlacesByCategoryUseCase
    private lateinit var viewModel: PlaceViewModel

    @Before
    fun setup() {
        getPlacesByCategoryUseCase = mockk()
        viewModel = PlaceViewModel(getPlacesByCategoryUseCase, testDispatcher)

        Dispatchers.setMain(testDispatcher)
        mockLogClass()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testSearchPlacesByCategory() =
        runTest(testDispatcher) {
            // given
            val categoryInput = "대형마트"
            val totalPageCount = 1
            val placeList =
                listOf(
                    Place(
                        id = "1234",
                        name = "마트 1",
                        category = "대형마트",
                        address = "주소1",
                        longitude = 12.3456,
                        latitude = 123.4567,
                    ),
                    Place(
                        id = "1235",
                        name = "마트 2",
                        category = "대형마트",
                        address = "주소2",
                        longitude = 12.3457,
                        latitude = 123.4568,
                    ),
                )

            coEvery {
                getPlacesByCategoryUseCase(
                    categoryInput,
                    totalPageCount,
                )
            } returns placeList

            // when
            viewModel.searchPlacesByCategory(categoryInput, totalPageCount)
            viewModel.searchResults.first {it.size == placeList.size}

            // then
            coVerify { getPlacesByCategoryUseCase(categoryInput, totalPageCount) }
            assertEquals(placeList, viewModel.searchResults.value)
        }

    @Test
    fun `searchPlacesByCategory는 대량의 데이터를 처리할 수 있다`() = runTest(testDispatcher) {
        // given
        val categoryInput = "대형마트"
        val totalPageCount = 1
        val largePlaceList = List(1000) { index ->
            Place(
                id = "1234$index",
                name = "마트 $index",
                category = "대형마트",
                address = "주소$index",
                longitude = 12.3456 + index * 0.0001,
                latitude = 123.4567 + index * 0.0001,
            )
        }

        coEvery {
            getPlacesByCategoryUseCase(
                categoryInput,
                totalPageCount,
            )
        } returns largePlaceList

        // when
        viewModel.searchPlacesByCategory(categoryInput, totalPageCount)
        viewModel.searchResults.first { it.size == 1000 }

        // then
        assertEquals(largePlaceList, viewModel.searchResults.value)
        coVerify { getPlacesByCategoryUseCase(categoryInput, totalPageCount) }
    }

    @Test
    fun `searchPlacesByCategory는 totalPageCount가 엄청 큰 경우를 처리할 수 있다`() = runTest(testDispatcher) {
        // given
        val categoryInput = "대형마트"
        val totalPageCount = Int.MAX_VALUE
        val placeList = listOf(
            Place(
                id = "1234",
                name = "마트 1",
                category = "대형마트",
                address = "주소1",
                longitude = 12.3456,
                latitude = 123.4567,
            ),
            Place(
                id = "1235",
                name = "마트 2",
                category = "대형마트",
                address = "주소2",
                longitude = 12.3457,
                latitude = 123.4568,
            ),
        )

        coEvery {
            getPlacesByCategoryUseCase(
                categoryInput,
                totalPageCount,
            )
        } returns placeList

        // when
        viewModel.searchPlacesByCategory(categoryInput, totalPageCount)
        viewModel.searchResults.first { it.size == 2 }

        // then
        assertEquals(placeList, viewModel.searchResults.value)
        coVerify { getPlacesByCategoryUseCase(categoryInput, totalPageCount) }
    }



    private fun mockLogClass() {
        mockkStatic(Log::class)
        every { Log.v(any(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }
}
