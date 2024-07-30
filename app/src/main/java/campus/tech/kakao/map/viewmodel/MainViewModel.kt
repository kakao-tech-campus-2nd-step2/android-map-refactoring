package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.UIState
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val placeRepository: PlaceRepository) : AndroidViewModel(application) {

    private val _uiState = MutableLiveData(UIState())
    val uiState: LiveData<UIState> get() = _uiState

    val searchQuery = MutableLiveData<String>()

    init {
        loadSavedSearches()
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val results = placeRepository.searchPlaces(query).map {document ->
                    Place(
                        name = document.place_name,
                        address = document.address_name,
                        category = document.category_group_name,
                        x = document.x,
                        y = document.y
                    )
                }
            _uiState.postValue(_uiState.value?.copy(
                searchResults = results,
                noResultsVisible = results.isEmpty(),
                searchRecyclerViewVisible = results.isNotEmpty(),
                savedSearchRecyclerViewVisible = results.isEmpty()
            ))
        }
    }

    fun addSearch(name: String, address: String, category: String, x: String, y: String) {
        viewModelScope.launch {
            val place = Place(name = name, address = address, category = category, x = x, y = y)
            placeRepository.savePlace(place)
            loadSavedSearches()
        }
    }

    fun removeSearch(name: String, address: String, category: String) {
        viewModelScope.launch {
            placeRepository.deletePlace(name, address, category)
            loadSavedSearches()
        }
    }

    fun loadSavedSearches() {
        viewModelScope.launch {
            val searches = placeRepository.getSavedPlaces()
            _uiState.postValue(_uiState.value?.copy(
                savedSearches = searches.reversed()
            ))
        }
    }

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }

    fun clearSearchQuery() {
        searchQuery.value = ""
    }

    fun clearSearchResults() {
        _uiState.postValue(_uiState.value?.copy(
            searchResults = emptyList(),
            noResultsVisible = true,
            searchRecyclerViewVisible = false,
            savedSearchRecyclerViewVisible = true
        ))
    }

    fun setSavedSearchRecyclerViewVisibility(visible: Boolean) {
        //_savedSearchRecyclerViewVisibility.value = visible
        _uiState.postValue(_uiState.value?.copy(
            savedSearchRecyclerViewVisible = visible
        ))
    }
}
