package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.PlaceRepository

class MapViewModelFactory (
    private val placeRepository: PlaceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapActivityViewModel::class.java)) {
            return MapActivityViewModel(placeRepository) as T
        }
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}