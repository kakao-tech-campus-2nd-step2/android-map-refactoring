package campus.tech.kakao.map.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val lastLocationRepository: LastLocationRepository
) : ViewModel() {
    private val _lastLocation = MutableLiveData<Location>()
    val lastLocation: LiveData<Location> = _lastLocation

    fun updateLastLocation() {
        viewModelScope.launch {
            _lastLocation.postValue(lastLocationRepository.getLastLocation())
        }
    }
}