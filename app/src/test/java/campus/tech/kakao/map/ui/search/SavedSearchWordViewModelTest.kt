package campus.tech.kakao.map.ui.search

import android.util.Log
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.domain.usecase.DeleteSearchWordByIdUseCase
import campus.tech.kakao.map.domain.usecase.GetAllSearchWordsUseCase
import campus.tech.kakao.map.domain.usecase.InsertOrUpdateSearchWordUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SavedSearchWordViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SavedSearchWordViewModel
    private lateinit var insertOrUpdateSearchWordUseCase: InsertOrUpdateSearchWordUseCase
    private lateinit var deleteSearchWordByIdUseCase: DeleteSearchWordByIdUseCase
    private lateinit var getAllSearchWordsUseCase: GetAllSearchWordsUseCase
    private lateinit var searchWord1: SavedSearchWordDomain
    private lateinit var searchWord2: SavedSearchWordDomain

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        insertOrUpdateSearchWordUseCase = mockk()
        deleteSearchWordByIdUseCase = mockk()
        getAllSearchWordsUseCase = mockk()
        viewModel = SavedSearchWordViewModel(
            insertOrUpdateSearchWordUseCase,
            deleteSearchWordByIdUseCase,
            getAllSearchWordsUseCase,
            testDispatcher,
        )

        searchWord1 =
            SavedSearchWordDomain(
                id = 1L,
                name = "부산대병원",
                placeId = "1234",
                address = "부산광역시 서구 구덕로 179",
                latitude = 123.456,
                longitude = 12.34,
            )
        searchWord2 =
            SavedSearchWordDomain(
                id = 2L,
                name = "부산대학교",
                placeId = "1235",
                address = "부산광역시 금정구 부산대학로63번길 2",
                latitude = 124.567,
                longitude = 23.45,
            )

        mockLogClass()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInsertOrUpdateSearchWord() =
        runTest(testDispatcher) {
            // Given
            coEvery { insertOrUpdateSearchWordUseCase(any()) } returns Unit
            coEvery { getAllSearchWordsUseCase() } returns listOf(searchWord1) andThen listOf(searchWord1, searchWord2)

            // When
            viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord1))
            viewModel.savedSearchWords.first { it.isNotEmpty() }
            viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord2))
            viewModel.savedSearchWords.first { it.size == 2 }

            // Then
            coVerify(exactly = 3) { getAllSearchWordsUseCase() }
            coVerify { insertOrUpdateSearchWordUseCase(searchWord1) }
            coVerify { insertOrUpdateSearchWordUseCase(searchWord2) }
            assertEquals(listOf(searchWord1, searchWord2), viewModel.savedSearchWords.value)
        }

    @Test
    fun testDeleteSearchWordById() =
        runTest(testDispatcher) {
            // Given
            coEvery { insertOrUpdateSearchWordUseCase(any()) } just runs
            coEvery { deleteSearchWordByIdUseCase(searchWord1.id) } just runs
            coEvery { getAllSearchWordsUseCase() } returns listOf(searchWord1) andThen listOf(
                searchWord1,
                searchWord2,
            ) andThen listOf(searchWord2)

            // When
            viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord1))
            viewModel.savedSearchWords.first { it.isNotEmpty() }

            viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord2))
            viewModel.savedSearchWords.first { it.size == 2 }

            viewModel.handleUiEvent(
                SavedSearchWordViewModel.UiEvent.OnSavedSearchWordClearImageViewClicked(searchWord1),
            )
            viewModel.savedSearchWords.first { it.size == 1 }

            // Then
            coVerify(exactly = 4) { getAllSearchWordsUseCase() }
            coVerify { deleteSearchWordByIdUseCase(searchWord1.id) }
            assertEquals(listOf(searchWord2), viewModel.savedSearchWords.value)
        }

    @Test
    fun testErrorMessageOnPlaceItemClicked() = runTest(testDispatcher) {
        // Given
        val exceptionMessage = "Test Exception"
        coEvery { insertOrUpdateSearchWordUseCase(any()) } throws RuntimeException(exceptionMessage)

        // When
        viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord1))

        // Then
        viewModel.errorMessage.first { it.equals("검색어 저장 중 에러가 발생하였습니다.") }
        assertEquals("검색어 저장 중 에러가 발생하였습니다.", viewModel.errorMessage.value)
    }

    @Test
    fun testErrorMessageOnSavedSearchWordClearImageViewClicked() = runTest(testDispatcher) {
        // Given
        val exceptionMessage = "Test Exception"
        coEvery { insertOrUpdateSearchWordUseCase(any()) } just runs
        coEvery { deleteSearchWordByIdUseCase(any()) } throws RuntimeException(exceptionMessage)
        coEvery { getAllSearchWordsUseCase() } returns emptyList()

        // When
        viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnPlaceItemClicked(searchWord1))
        viewModel.handleUiEvent(SavedSearchWordViewModel.UiEvent.OnSavedSearchWordClearImageViewClicked(searchWord1))

        // Then
        viewModel.errorMessage.first { it.equals("검색어 삭제 중 에러가 발생하였습니다.") }
        assertEquals("검색어 삭제 중 에러가 발생하였습니다.", viewModel.errorMessage.value)
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
