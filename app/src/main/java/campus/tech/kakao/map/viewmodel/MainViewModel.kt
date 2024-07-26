package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import retrofit2.Call
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.Model.KakaoLocalApi
import campus.tech.kakao.map.Model.LocationDao
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Model.Place
import campus.tech.kakao.map.Model.SearchCallback
import campus.tech.kakao.map.Model.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationDao: LocationDao,
    private val kakaoLocalApi: KakaoLocalApi
) : ViewModel() {

    private var listener: (UiState) -> Unit = {}
    private lateinit var call: Call<SearchResult>

    private var uiState: UiState = UiState(
        locationList = emptyList(),
        isShowText = false
    )

    fun setUiStateChangedListener(listener: (UiState) -> Unit) {
        this.listener = listener
    }

    fun searchLocations(key: String) {
        call = kakaoLocalApi.searchPlaces(BuildConfig.API_KEY, key)
        call.enqueue(SearchCallback(this))
    }

    fun updateSearchResults(results: List<Place>) {
        val locationList = results.map { result ->
            LocationData(
                result.place_name,
                result.address_name,
                result.category_group_name,
                result.y.toDouble(),
                result.x.toDouble()
            )
        }
        uiState = UiState(
            locationList = locationList,
            isShowText = locationList.isEmpty()
        )

        listener(uiState)
    }

    // DB 관련 메서드들
    fun insertLocation(location: LocationData) {
        locationDao.insertLocation(location)
    }

    fun getAllLocations(): List<LocationData> {
        return locationDao.getAllLocations()
    }

    fun deleteLocation(location: LocationData) {
        locationDao.deleteLocation(location)
    }

    fun deleteAllLocations() {
        locationDao.deleteAllLocations()
    }

    override fun onCleared() {
        super.onCleared()
        if (::call.isInitialized) {
            call.cancel()
        }
    }

    data class UiState(
        val locationList: List<LocationData>,
        val isShowText: Boolean
    )
}