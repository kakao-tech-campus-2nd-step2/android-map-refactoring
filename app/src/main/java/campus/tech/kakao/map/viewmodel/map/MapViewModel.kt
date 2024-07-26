package campus.tech.kakao.map.viewmodel.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.model.state.UiState
import campus.tech.kakao.map.repository.map.MapRepository

class MapViewModel(private val mapRepository: MapRepository) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>(UiState.NotInitialized)
    val uiState: LiveData<UiState>
        get() = _uiState

    fun showErrorView(e: Exception) {
        _uiState.value = UiState.Error(e)
    }

    fun showSuccessView() {
        _uiState.value = UiState.Success
    }

    fun updateLastPosition(latitude: Double, longitude: Double) {
        mapRepository.updateLastPosition(latitude, longitude)
    }

    fun readLastPosition(): Pair<Double?, Double?> {
        return mapRepository.readLastPosition()
    }
}
