package campus.tech.kakao.map.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.room.SearchHistoryData
import campus.tech.kakao.map.dataRepository.SearchHistoryRepository
import kotlinx.coroutines.launch

class DBViewModel(private val searchHistoryRepo: SearchHistoryRepository) : ViewModel() {

    private val _searchHistoryDataList = MutableLiveData<List<SearchHistoryData>>()
    private val searchHistoryDataList: LiveData<List<SearchHistoryData>> = _searchHistoryDataList

    init {
        viewModelScope.launch {
            _searchHistoryDataList.value = searchHistoryRepo.getSearchHistory()
        }
    }

    suspend fun addRecentSearchItem(name: String, address: String, time: Long) {
        viewModelScope.launch {
            val exitData = searchHistoryRepo.findSearchItem(name, address)

            if (exitData != null) {
                searchHistoryRepo.updateTime(name, address, time)
            } else {
                searchHistoryRepo.insertSearchData(name, address, time)
            }
        }
    }

    fun getRecentDataLiveData(): LiveData<List<SearchHistoryData>> {
        return searchHistoryDataList
    }

    suspend fun deleteRecentData(data: String, address: String, time: Long) {
        Log.d("yeong","DBViewModel: 여기까지 옴")
        searchHistoryRepo.deleteSearchData(data, address, time)
        _searchHistoryDataList.value = searchHistoryRepo.getSearchHistory()
    }
}