package campus.tech.kakao.map.presentation.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.launch

class MapViewModel(private val repository: PlaceRepository): ViewModel() {

    private val _lastVisitedPlace = MutableLiveData<Place?>()
    val lastVisitedPlace: LiveData<Place?> get() = _lastVisitedPlace
    fun loadLastVisitedPlace() {
        viewModelScope.launch {
            val place = repository.getLastVisitedPlace()
            _lastVisitedPlace.value = place
        }
    }
    fun saveLastVisitedPlace(place: Place) {
        viewModelScope.launch {
            repository.saveLastVisitedPlace(place)
            _lastVisitedPlace.value = place
        }
    }
}