package campus.tech.kakao.map.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val resultRepository: ResultRepository,
    private val historyRepository: HistoryRepository,
    private val lastLocationRepository: LastLocationRepository
) : ViewModel() {
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> get() = _searchResult
    private val _searchHistory = MutableLiveData<List<History>>()
    val searchHistory: LiveData<List<History>> get() = _searchHistory
    private lateinit var prevJob: Job

    fun searchKeyword(keyword: String) {
        if (::prevJob.isInitialized)
            prevJob.cancel()
        prevJob = viewModelScope.launch {
            resultRepository.search(keyword)
            _searchResult.value = resultRepository.getAllResult()
        }
    }

    fun getAllResult(): List<Location> {
        return resultRepository.getAllResult()
    }

    fun insertHistory(newHistory: History) {
        viewModelScope.launch {
            historyRepository.insertHistory(newHistory)
            _searchHistory.value = historyRepository.getAllHistory()
        }
    }

    fun deleteHistory(oldHistory: History) {
        viewModelScope.launch {
            historyRepository.deleteHistory(oldHistory)
            _searchHistory.value = historyRepository.getAllHistory()
        }
    }

    fun getAllHistory(): List<History> {
        viewModelScope.launch {
            _searchHistory.value = historyRepository.getAllHistory()
        }
        return _searchHistory.value ?: listOf()
    }

    fun insertLastLocation(location: Location) {
        viewModelScope.launch {
            lastLocationRepository.insertLastLocation(location)
        }
    }
}