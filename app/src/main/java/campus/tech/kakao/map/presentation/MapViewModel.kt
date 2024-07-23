package campus.tech.kakao.map.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import campus.tech.kakao.map.MapApplication
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.data.source.MapDbHelper
import campus.tech.kakao.map.data.source.RetrofitService
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.repository.HistoryRepository
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import campus.tech.kakao.map.domain.repository.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class MapViewModel @Inject constructor(
    private val resultRepository: ResultRepository,
    private val historyRepository: HistoryRepository,
    private val lastLocationRepository: LastLocationRepository
) : ViewModel() {
    private val _searchResult = MutableLiveData<List<Location>>()
    val searchResult: LiveData<List<Location>> = _searchResult
    private val _searchHistory = MutableLiveData<List<History>>()
    val searchHistory: LiveData<List<History>> = _searchHistory
    private val _lastLocation = MutableLiveData<Location>()
    val lastLocation: LiveData<Location> = _lastLocation

    fun searchKeyword(keyword: String) {
        viewModelScope.launch {
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
        updateLastLocation()
    }

    fun updateLastLocation() {
        viewModelScope.launch {
            _lastLocation.postValue(lastLocationRepository.getLastLocation())
        }
    }

}