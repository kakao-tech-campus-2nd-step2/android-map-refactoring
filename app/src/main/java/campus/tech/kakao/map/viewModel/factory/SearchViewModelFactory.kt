package campus.tech.kakao.map.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.SearchResultRepository import campus.tech.kakao.map.viewModel.SearchViewModel
import java.lang.IllegalArgumentException

class SearchViewModelFactory(private val searchResultRepo: SearchResultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(searchResultRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}