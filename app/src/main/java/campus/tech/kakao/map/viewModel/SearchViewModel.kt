package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.SearchResultRepository
import campus.tech.kakao.map.retrofit.Document

class SearchViewModel(private val repository: SearchResultRepository) : ViewModel() {
    private val _searchDataList = MutableLiveData<List<Document>>()
    val searchResults: LiveData<List<Document>> get() = _searchDataList

    fun loadResultData(searchQuery: String) {
        repository.loadResultMapData(searchQuery) { documents ->
            _searchDataList.postValue(documents)
        }
    }

    fun getSearchDataLiveData(): LiveData<List<Document>> {
        return searchResults
    }
}