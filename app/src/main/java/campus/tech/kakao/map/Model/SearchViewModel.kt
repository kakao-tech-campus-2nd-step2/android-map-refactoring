package campus.tech.kakao.map.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import campus.tech.kakao.map.Adapter.PlaceAdapter
import campus.tech.kakao.map.Adapter.SavedSearchAdapter
import campus.tech.kakao.map.Data.Place
import campus.tech.kakao.map.Data.SearchRepository
import campus.tech.kakao.map.Data.SearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val _searchQuery = MutableLiveData<String?>()
    val searchQuery: LiveData<String?> get() = _searchQuery
    private val _isSavedSearchesVisible = MutableLiveData<Boolean>()
    val isSavedSearchesVisible: LiveData<Boolean> get() = _isSavedSearchesVisible
    private val _searchResults = MutableLiveData<List<SearchResult>?>()
    val searchResults: LiveData<List<SearchResult>?> get() = _searchResults
    private var _savedSearches = MutableLiveData<List<Place>>()
    val savedSearches: LiveData<List<Place>> get() = _savedSearches
    val savedSearchAdapter = SavedSearchAdapter()
    val searchResultAdapter = PlaceAdapter()

    init {
        _isSavedSearchesVisible.value = false
        loadSavedSearches()
    }

    fun onSearchQuerySubmitted() {
        _searchQuery.value?.let { query ->
            searchResults(query)
        }
    }

    fun onSearchQueryChanged(newText: String?) {
        _searchQuery.value = newText
        _isSavedSearchesVisible.value = !newText.isNullOrEmpty()
        searchResults(newText)
    }

    private fun searchResults(query: String?) {
        viewModelScope.launch {
            val results = query?.let { searchRepository.getSearchResults(it) }
            _searchResults.value = results
            searchResultAdapter.submitList(results)
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            val savedSearches = String.let { searchRepository.getSearchResults(savedSearches.toString()) }
            _savedSearches = MutableLiveData<List<Place>>()
            savedSearchAdapter.updateData(savedSearches.map { it.text }.toString())
        }
    }
}










