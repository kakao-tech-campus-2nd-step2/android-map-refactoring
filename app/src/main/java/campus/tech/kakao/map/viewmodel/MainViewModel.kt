package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.Repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    fun searchLocations(query: String) {
        viewModelScope.launch {
            val results = repository.searchLocations(query)
            _uiState.value = UiState(
                locationList = results,
                isShowText = results.isEmpty()
            )
        }
    }

    data class UiState(
        val locationList: List<LocationData> = emptyList(),
        val isShowText: Boolean = false
    )
}