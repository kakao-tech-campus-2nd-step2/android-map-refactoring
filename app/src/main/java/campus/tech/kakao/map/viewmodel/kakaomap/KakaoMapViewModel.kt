package campus.tech.kakao.map.viewmodel.kakaomap

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.kakaomap.LastPositionRepository
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class KakaoMapViewModel @Inject constructor(
    private var lastPositionRepository: LastPositionRepository
) : ViewModel() {
    private val _lastPosition = MutableStateFlow<LatLng?>(null)
    val lastPosition: StateFlow<LatLng?> get() = _lastPosition

    init {
        loadLastPosition()
    }

    fun saveLastPosition(position: LatLng) {
        lastPositionRepository.saveLastPosition(position)
    }

    fun loadLastPosition() {
        _lastPosition.value = lastPositionRepository.loadLastPosition()
    }
}