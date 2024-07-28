package campus.tech.kakao.map.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.LastVisitedPlaceManager
import campus.tech.kakao.map.domain.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel
@Inject
constructor(private val manager: LastVisitedPlaceManager): ViewModel() {

    private val _lastVisitedPlace = MutableStateFlow<Place?>(null)
    val lastVisitedPlace: StateFlow<Place?> get() = _lastVisitedPlace.asStateFlow()
    
    fun loadLastVisitedPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            val place = manager.getLastVisitedPlace()
            _lastVisitedPlace.value = place
        }
    }
    
    fun saveLastVisitedPlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            manager.saveLastVisitedPlace(place)
            _lastVisitedPlace.value = place
        }
    }
}