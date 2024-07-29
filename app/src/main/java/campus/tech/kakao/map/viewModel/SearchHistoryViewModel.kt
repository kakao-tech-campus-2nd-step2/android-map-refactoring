package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.room.SearchHistoryData
import campus.tech.kakao.map.repository.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val searchHistoryRepo: SearchHistoryRepository
) : ViewModel() {

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

    suspend fun deleteRecentData(data: String, address: String) {
        searchHistoryRepo.deleteSearchData(data, address)
        _searchHistoryDataList.value = searchHistoryRepo.getSearchHistory()
    }
}