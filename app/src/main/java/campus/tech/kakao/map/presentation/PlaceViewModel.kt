package campus.tech.kakao.map.presentation

import campus.tech.kakao.map.domain.usecase.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.PlaceVO
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val getSearchPlacesUseCase: GetSearchPlacesUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val removeSearchQueryUseCase: RemoveSearchQueryUseCase,
) : ViewModel() {
    private val _places = MutableLiveData<List<PlaceVO>>()
    val places: LiveData<List<PlaceVO>> get() = _places
    private val _searchHistory = MutableLiveData<List<String>>()
    val searchHistory: LiveData<List<String>> get() = _searchHistory

    fun searchPlaces(query: String) {
        getSearchPlacesUseCase(query) {
            _places.postValue(it)
        }
    }

    fun saveSearchQuery(place: PlaceVO) {
        viewModelScope.launch(Dispatchers.IO) {
            saveSearchQueryUseCase(place)
            loadSearchHistory()
        }
    }

    fun loadSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            val history = getSearchHistoryUseCase()
            _searchHistory.postValue(history.toList())
        }
    }

    fun removeSearchQuery(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            removeSearchQueryUseCase(query)
            loadSearchHistory()
        }
    }

    fun getPlaceLocation(place: PlaceVO): LatLng {
        // PlaceVO에서 위치 정보를 가져오는 로직
        return LatLng.from(place.latitude, place.longitude)
    }

}