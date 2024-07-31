package campus.tech.kakao.map.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.SavedPlace
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.SavedPlaceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test

class SearchActivityViewModelTest{

    private lateinit var placeRepository: PlaceRepository
    private lateinit var savedPlaceRepository : SavedPlaceRepository
    private lateinit var viewModel : SearchActivityViewModel
    private lateinit var observerPlace: Observer<List<Place>>
    private lateinit var observerSavedPlace : Observer<List<SavedPlace>>

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        placeRepository = mockk(relaxed = true)
        savedPlaceRepository = mockk(relaxed = true)
        coEvery { savedPlaceRepository.getAllSavedPlace() } returns emptyList()
        viewModel = SearchActivityViewModel(placeRepository, savedPlaceRepository)

        observerPlace = mockk(relaxed = true)
        observerSavedPlace = mockk(relaxed = true)
        viewModel.place.observeForever(observerPlace)
        viewModel.savedPlace.observeForever(observerSavedPlace)

    }

    @Test
    fun `PlaceRepository에서 데이터를 제대로 요청하고 있는지 검사`() = runTest{

        val text = "test"
        coEvery { placeRepository.getKakaoLocalPlaceData(text) } returns emptyList()

        viewModel.getKakaoLocalData(text)
        coVerify { placeRepository.getKakaoLocalPlaceData(text) }
    }

    @Test
    fun `SavedPlaceRepository에서 데이터를 제대로 요청하고 있는지 검사`() = runTest{
        viewModel.getSavedPlace()
        coVerify { savedPlaceRepository.getAllSavedPlace() }
    }

    @Test
    fun `Place 데이터를 저장하기 위해 Repository를 호출하고 있는지 검사`() = runTest{
        val place : Place = mockk()
        viewModel.savePlace(place)
        coVerify { savedPlaceRepository.writePlace(place) }
        coVerify { savedPlaceRepository.getAllSavedPlace() }
    }

    @Test
    fun `Place 데이터를 삭제하기 위해 Repository를 호출하고 있는지 검사`() = runTest{
        val savedPlace : SavedPlace = mockk()
        viewModel.deleteSavedPlace(savedPlace)
        coVerify { savedPlaceRepository.deleteSavedPlace(savedPlace) }
        coVerify { savedPlaceRepository.getAllSavedPlace() }
    }

    @After
    fun removeObserver(){
        viewModel.place.removeObserver(observerPlace)
        viewModel.savedPlace.removeObserver(observerSavedPlace)
    }
}