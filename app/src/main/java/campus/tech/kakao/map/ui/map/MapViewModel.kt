package campus.tech.kakao.map.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.last_location.LastLocationRepository
import kotlinx.coroutines.launch
import java.io.IOException

class MapViewModel(
    private val lastLocationRepository: LastLocationRepository
) : ViewModel() {

    fun saveLastLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                lastLocationRepository.saveLocation(latitude, longitude)
            } catch (ioException: IOException) {
                Log.e("MapViewModel", "Failed to save location", ioException)
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    fun loadLastLocation(callback: (Double, Double, Boolean) -> Unit) {
        viewModelScope.launch {
            lastLocationRepository.loadLocation().collect {
                it?.let {
                    callback(it.latitude, it.longitude, false)
                }
            }
        }
    }
}