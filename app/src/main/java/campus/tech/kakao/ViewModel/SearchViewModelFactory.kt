package campus.tech.kakao.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.Model.RoomDb

class SearchViewModelFactory(private val db: RoomDb) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(db) as T
    }
}