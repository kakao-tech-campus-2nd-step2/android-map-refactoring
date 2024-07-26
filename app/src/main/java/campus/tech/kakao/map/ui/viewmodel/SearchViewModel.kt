package campus.tech.kakao.map.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.db.SearchHistory
import campus.tech.kakao.map.data.db.SearchHistoryDao
import campus.tech.kakao.map.data.remote.api.KakaoLocalApi
import campus.tech.kakao.map.data.remote.api.dto.Place
import campus.tech.kakao.map.repository.KakaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kakaoRepository: KakaoRepository,
    private val searchHistoryDao: SearchHistoryDao
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Place>>()
    val searchResults: LiveData<List<Place>> get() = _searchResults

    private val _searchHistory = MutableLiveData<List<SearchHistory>>()
    val searchHistory: LiveData<List<SearchHistory>> get() = _searchHistory

    private val _mapX = MutableLiveData<String>()
    val mapX: LiveData<String> get() = _mapX

    private val _mapY = MutableLiveData<String>()
    val mapY: LiveData<String> get() = _mapY

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    init {
        loadSearchHistory()
    }

    private fun loadSearchHistory() {
        viewModelScope.launch {
            _searchHistory.value = searchHistoryDao.getAllSearchHistory()
        }
    }

    fun searchPlaces(query: String) {
        kakaoRepository.searchPlaces(query) { places ->
            _searchResults.postValue(places)
        }
    }

    fun addSearchHistory(searchHistory: SearchHistory) {
        viewModelScope.launch {
            searchHistoryDao.insertSearchHistory(searchHistory)
            _searchHistory.value = searchHistoryDao.getAllSearchHistory()
        }
    }

    fun deleteSearchHistory(searchHistory: SearchHistory) {
        viewModelScope.launch {
            searchHistoryDao.deleteSearchHistoryById(searchHistory.id)
            _searchHistory.value = searchHistoryDao.getAllSearchHistory()
        }
    }

    fun updateMapPosition(place: Place) {
        _mapX.value = place.x
        _mapY.value = place.y
        _name.value = place.place_name
        _address.value = place.address_name
    }
}
