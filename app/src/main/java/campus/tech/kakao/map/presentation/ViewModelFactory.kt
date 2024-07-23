package campus.tech.kakao.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.domain.repository.PlaceRepository
import campus.tech.kakao.map.presentation.map.MapViewModel
import campus.tech.kakao.map.presentation.search.SearchViewModel


class ViewModelFactory(private val repository: PlaceRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MapViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}