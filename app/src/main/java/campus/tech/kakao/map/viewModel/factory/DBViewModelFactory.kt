package campus.tech.kakao.map.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.repository.SearchHistoryRepository
import campus.tech.kakao.map.viewModel.DBViewModel
import java.lang.IllegalArgumentException

class DBViewModelFactory(repository: SearchHistoryRepository) : ViewModelProvider.Factory {
    private val dbRepo = repository

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DBViewModel::class.java)) {
            return DBViewModel(dbRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}