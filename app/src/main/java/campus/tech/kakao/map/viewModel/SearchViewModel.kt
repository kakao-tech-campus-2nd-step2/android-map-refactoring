package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.SearchResultRepository
import campus.tech.kakao.map.retrofit.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchResultRepository
) : ViewModel() {

    private val _searchDataList = MutableLiveData<List<Document>>()
    private val searchResults: LiveData<List<Document>> get() = _searchDataList

    fun loadResultData(searchQuery: String) {
        repository.loadResultMapData(searchQuery) { documents ->
            _searchDataList.postValue(documents)
        }
    }

    fun getSearchDataLiveData(): LiveData<List<Document>> {
        return searchResults
    }
}