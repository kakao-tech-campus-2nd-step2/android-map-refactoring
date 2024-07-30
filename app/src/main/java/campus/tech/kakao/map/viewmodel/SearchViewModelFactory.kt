package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.model.CoroutineIoDispatcher
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository
import kotlinx.coroutines.CoroutineDispatcher

class SearchViewModelFactory(
    private val placeRepository: PlaceRepository,
    private val savedPlaceRepository: SavedPlaceRepository,
    @CoroutineIoDispatcher private val IoDispatcher : CoroutineDispatcher
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchActivityViewModel::class.java)) {
            return SearchActivityViewModel(placeRepository, savedPlaceRepository, IoDispatcher) as T
        }
        throw IllegalArgumentException("unKnown ViewModel class")
    }
}