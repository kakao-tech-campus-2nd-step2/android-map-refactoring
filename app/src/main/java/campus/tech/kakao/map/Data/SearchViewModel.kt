package campus.tech.kakao.map.Data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SearchRepository

    init {
        val searchDao = AppDatabase.getDatabase(application).searchDao()
        repository = SearchRepository(searchDao)
    }

    fun insertSearchResult(text: String) = viewModelScope.launch {
        repository.insertSearchResult(text)
    }

    fun deleteSearchResult(searchResult: SearchResult) = viewModelScope.launch {
        repository.deleteSearchResult(searchResult)
    }

    private val searchresults = MutableLiveData<List<SearchResult>>()
    val searchResults: LiveData<List<SearchResult>> = searchresults

    fun getSearchResults(text: String) = viewModelScope.launch {
        searchresults.postValue(repository.getSearchResults(text))
    }
}





