package campus.tech.kakao.map.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.history.History
import campus.tech.kakao.map.data.local_search.Location
import campus.tech.kakao.map.domain.usecase.GetHistoryUseCase
import campus.tech.kakao.map.domain.usecase.RemoveHistoryUseCase
import campus.tech.kakao.map.domain.usecase.SearchLocationUseCase
import campus.tech.kakao.map.domain.usecase.UpdateHistoryUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchLocationViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val updateHistoryUseCase: UpdateHistoryUseCase,
    private val removeHistoryUseCase: RemoveHistoryUseCase,
    private val searchLocationUseCase: SearchLocationUseCase
) : ViewModel() {

    private val _location = MutableLiveData<List<Location>?>(emptyList())
    val location: LiveData<List<Location>?> = _location

    private val _history = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> = _history

    private val _searchInput = MutableLiveData<String>()
    val searchInput: LiveData<String> = _searchInput

    private val _markerLocation = MutableLiveData<Location>()
    val markerLocation: LiveData<Location> = _markerLocation

    init {
        viewModelScope.launch {
            _history.postValue(getHistoryUseCase())
        }
    }

    fun searchLocationByHistory(locationName: String) {
        _searchInput.postValue(locationName)
    }

    fun searchLocation(category: String) {
        viewModelScope.launch {
            _location.postValue(searchLocationUseCase(category))
        }
    }

    fun addHistory(locationName: String) {
        viewModelScope.launch {
            updateHistoryUseCase(locationName)
            _history.postValue(getHistoryUseCase())
        }
    }

    fun removeHistory(locationName: String) {
        viewModelScope.launch {
            removeHistoryUseCase(locationName)
            _history.postValue(getHistoryUseCase())
        }
    }

    fun addMarker(location: Location) {
        _markerLocation.postValue(location)
    }
}