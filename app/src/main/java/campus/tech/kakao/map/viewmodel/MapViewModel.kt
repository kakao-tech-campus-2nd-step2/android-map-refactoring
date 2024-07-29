package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.MapItemEntity
import campus.tech.kakao.map.repository.MapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    private val repository: MapRepository
) : AndroidViewModel(application) {

    val searchQuery = MutableLiveData<String>()

    private val _searchResults = MutableLiveData<List<MapItemEntity>>(emptyList())
    val searchResults: LiveData<List<MapItemEntity>> = _searchResults

    private val _selectedItems = MutableLiveData<List<MapItemEntity>>(emptyList())
    val selectedItems: LiveData<List<MapItemEntity>> get() = _selectedItems

    init {
        searchQuery.observeForever { query ->
            if (query.isNullOrEmpty()) {
                _searchResults.postValue(emptyList())
            } else {
                searchItems(query)
            }
        }
    }

    private fun searchItems(query: String) {
        viewModelScope.launch {
            val results = repository.searchItems(query)
            _searchResults.postValue(results)
        }
    }

    fun selectItem(item: MapItemEntity) {
        _selectedItems.value = _selectedItems.value?.plus(item)
    }

    fun removeSelectedItem(item: MapItemEntity) {
        _selectedItems.value = _selectedItems.value?.minus(item)
    }

    fun setSelectedItems(items: List<MapItemEntity>) {
        _selectedItems.value = items
    }

    fun setSearchResults(results: List<MapItemEntity>) {
        _searchResults.value = results
    }

    fun clearSearchQuery() {
        searchQuery.value = ""
    }
}