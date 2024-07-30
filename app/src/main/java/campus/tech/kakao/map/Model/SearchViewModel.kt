package campus.tech.kakao.map.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.Data.Document
import campus.tech.kakao.map.Data.Place
import campus.tech.kakao.map.Data.ResultSearchKeyword
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

    init {
        _isSavedSearchesVisible.value = false
        loadSavedSearches()
    }

    fun onSearchQuerySubmitted() {
        searchQuery.value?.let { query ->
            searchResults(query)
        }
    }

    fun onSearchQueryChanged(newText: String) {
        searchQuery.value = newText
        _isSavedSearchesVisible.value = newText.isNotEmpty()
        searchResults(newText)
    }

    private fun searchResults(query: String?) {
        viewModelScope.launch {
            val results = query?.let { searchRepository.getSearchResults(it) }
            _searchResults.value = results
        }
    }

    private fun loadSavedSearches() {
        viewModelScope.launch {
            val searchResults = searchRepository.getSearchResults(savedSearches.toString())
            //_savedSearches.value = searchResults
            //여기서 자꾸 에러가 발생하는데 이걸 어떻게 해결해야할 지 모르겠습니다.
        }
    }

}













