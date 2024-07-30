package campus.tech.kakao.map.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val searchQuery = MutableLiveData<String>("")

    private val _isSavedSearchesVisible = MutableLiveData<Boolean>()
    val isSavedSearchesVisible: LiveData<Boolean> get() = _isSavedSearchesVisible

    private val _searchResults = MutableLiveData<List<SearchResult>?>()
    val searchResults: LiveData<List<SearchResult>?> get() = _searchResults

    private val _savedSearches = MutableLiveData<List<Place>>()
    val savedSearches: LiveData<List<Place>> get() = _savedSearches

    val savedSearchAdapter = SavedSearchAdapter()
    val searchResultAdapter = PlaceAdapter()

    init {
        _isSavedSearchesVisible.value = false
        loadSavedSearches()
    }

    fun onSearchQuerySubmitted() {
        searchQuery.value?.let { query ->
            searchResults(query)
        }
    }

    fun onSearchQueryChanged(newText: String) { // 여기를 String 타입으로 변경
        searchQuery.value = newText
        _isSavedSearchesVisible.value = !newText.isNullOrEmpty()
        searchResults(newText)
    }

    private fun searchResults(query: String?) {
        viewModelScope.launch {
            val results = query?.let { searchRepository.getSearchResults(it) }
            _searchResults.value = results
            searchResultAdapter.submitList(results ?: emptyList())
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            val savedSearches = searchRepository.getSearchResults(savedSearches.toString())
            savedSearchAdapter.submitList(savedSearches)
        }
    }
}












