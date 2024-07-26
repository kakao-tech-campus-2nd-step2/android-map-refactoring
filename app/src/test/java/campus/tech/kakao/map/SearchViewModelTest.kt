package campus.tech.kakao.map

import android.content.Context
import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var preferenceManager: FakePreferenceManager
    private lateinit var repository: FakeRetrofitRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val context = ApplicationProvider.getApplicationContext<Context>()
        preferenceManager = FakePreferenceManager(context)
        repository = FakeRetrofitRepository()
        viewModel = SearchViewModel(context, preferenceManager, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun getPlace_setsLocationList_and_updatesEmptyTextVisibility() = testScope.runBlockingTest {
        // Given
        val searchText = "Place"
        val documents = listOf(
            Document(
                addressName = "Address1",
                categoryGroupCode = "CategoryGroupCode1",
                categoryGroupName = "CategoryGroupName1",
                categoryName = "CategoryName1",
                distance = "Distance1",
                id = "Id1",
                phone = "Phone1",
                placeName = "Place1",
                placeUrl = "PlaceUrl1",
                roadAddressName = "RoadAddressName1",
                x = "X1",
                y = "Y1"
            )
        )
        repository.setPlaces(documents)

        // When
        viewModel.getPlace(searchText)

        // Then
        advanceUntilIdle() // 모든 코루틴이 완료될 때까지 기다립니다.

        val locationList = viewModel.locationList.value
        assertEquals(1, locationList?.size)
        assertEquals("Place1", locationList?.get(0)?.placeName)

        val emptyTextVisibility = viewModel.emptyMainTextVisibility.value
        assertFalse(emptyTextVisibility ?: true)
    }

    @Test
    fun getPlace_withEmptyResult_updatesEmptyTextVisibility() = testScope.runBlockingTest {
        // Given
        val searchText = "NonExistentPlace"
        repository.setPlaces(emptyList())

        // When
        viewModel.getPlace(searchText)

        // Then
        advanceUntilIdle() // 모든 코루틴이 완료될 때까지 기다립니다.

        val locationList = viewModel.locationList.value
        assertTrue(locationList?.isEmpty() ?: false)

        val emptyTextVisibility = viewModel.emptyMainTextVisibility.value
        assertTrue(emptyTextVisibility ?: false)
    }

    // FakePreferenceManager 클래스 추가
    class FakePreferenceManager(context: Context) : PreferenceManager(context) {
        private val searchHistory = mutableListOf<SearchHistory>()

        override fun getArrayList(key: String): ArrayList<SearchHistory> {
            return ArrayList(searchHistory)
        }

        override fun savePreference(key: String, searchHistory: SearchHistory, currentList: ArrayList<SearchHistory>) {
            this.searchHistory.add(searchHistory)
        }

        override fun deleteArrayListItem(key: String, index: Int) {
            if (index >= 0 && index < searchHistory.size) {
                searchHistory.removeAt(index)
            }
        }

        fun addSearchHistory(searchHistory: SearchHistory) {
            this.searchHistory.add(searchHistory)
        }

        fun clearSearchHistory() {
            this.searchHistory.clear()
        }
    }

    // FakeRetrofitRepository 클래스 추가
    class FakeRetrofitRepository : RetrofitRepository(RetrofitInstance.retrofitService) {
        private var places = listOf<Document>()

        fun setPlaces(places: List<Document>) {
            this.places = places
        }

        override suspend fun getPlace(query: String): List<Document> {
            return places
        }
    }
}
