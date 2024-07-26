package campus.tech.kakao.map.viewmodel.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.location.KakaoLocationImpl

class SearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(KakaoLocationImpl.getInstance()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
