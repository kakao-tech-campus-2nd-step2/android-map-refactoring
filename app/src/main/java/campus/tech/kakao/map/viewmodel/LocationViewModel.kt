package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.repository.DefaultLocationRepository
import campus.tech.kakao.map.model.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()

    private val _searchedLocations = MutableLiveData<List<Location>>()
    val searchedLocations: LiveData<List<Location>> get() = _searchedLocations

    private fun getSearchedLocationsSize(): Int {
        return _searchedLocations.value?.size ?: 0
    }

    fun setLocationsFromKakaoAPI() {
        _locations.value = emptyList()
        _searchedLocations.value = emptyList()
    }

    fun searchLocationsFromKakaoAPI(query: String, handleNoResultMessage: (Int) -> Unit) {
        viewModelScope.launch {
            _searchedLocations.value = locationRepository.getLocationAll(query)
            handleNoResultMessage(getSearchedLocationsSize())
        }
    }
    fun addLastLocation(location: Location){
        locationRepository.addLastLocation(location)
    }

    fun getLastLocation(): Location? {
        return locationRepository.getLastLocation()
    }

}