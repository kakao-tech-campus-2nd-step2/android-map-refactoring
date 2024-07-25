package campus.tech.kakao.map.ui.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.usecase.LoadLastLocationUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastLocationUseCase
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val saveLastLocationUseCase: SaveLastLocationUseCase,
    private val loadLastLocationUseCase: LoadLastLocationUseCase
) : ViewModel() {

    fun saveLastLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                saveLastLocationUseCase(latitude, longitude)
            } catch (ioException: IOException) {
                Log.e("MapViewModel", "Failed to save location", ioException)
            } catch (exception: Exception) {
                throw exception
            }
        }
    }

    fun loadLastLocation(callback: (Double, Double, Boolean) -> Unit) {
        viewModelScope.launch {
            loadLastLocationUseCase().collect {
                it?.let {
                    callback(it.latitude, it.longitude, false)
                }
            }
        }
    }
}