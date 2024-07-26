package campus.tech.kakao.map.Model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import campus.tech.kakao.map.Adapter.PlaceAdapter
import campus.tech.kakao.map.Adapter.SavedSearchAdapter
import campus.tech.kakao.map.Data.AppDatabase
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

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _isSavedSearchesVisible = MutableLiveData<Boolean>()
    val isSavedSearchesVisible: LiveData<Boolean> get() = _isSavedSearchesVisible


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
            val results = searchRepository.insertSearchResult(query)
            searchResultAdapter.submitList(results)
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            val savedSearches = searchRepository.getSearchResults()
            savedSearchAdapter.notifyDataSetChanged()
        }
    }
}









