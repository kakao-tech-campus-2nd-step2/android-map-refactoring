package campus.tech.kakao.map.viewmodel.kakaomap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.kakaomap.LastPositionRepository

class KakaoMapViewModelFactory(
    private val lastPositionRepository: LastPositionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return KakaoMapViewModel(lastPositionRepository) as T
    }
}