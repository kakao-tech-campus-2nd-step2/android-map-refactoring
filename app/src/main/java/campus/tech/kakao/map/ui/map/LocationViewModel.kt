package campus.tech.kakao.map.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.di.IoDispatcher
import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.domain.usecase.LoadLocationUseCase
import campus.tech.kakao.map.domain.usecase.SaveLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel
@Inject
constructor(
    private val loadLocationUseCase: LoadLocationUseCase,
    private val saveLocationUseCase: SaveLocationUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _location = MutableStateFlow(getDefaultLocation())
    val location: StateFlow<LocationDomain> get() = _location

    init {
        loadLocation()
    }

    fun saveLocation(newLocation: LocationDomain) {
        viewModelScope.launch(ioDispatcher) {
            try {
                saveLocationUseCase(newLocation)
                _location.value = newLocation
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error saving location", e)
            }
        }
    }

    private fun loadLocation() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val loadedLocation = loadLocationUseCase()
                _location.value = loadedLocation
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error loading location", e)
                _location.value = getDefaultLocation()
            }
        }
    }

    private fun getDefaultLocation(): LocationDomain {
        return LocationDomain(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )
    }
}
