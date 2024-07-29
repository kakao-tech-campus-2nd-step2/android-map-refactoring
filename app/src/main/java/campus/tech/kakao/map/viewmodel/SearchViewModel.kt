package campus.tech.kakao.map.viewmodel

import android.content.Intent
import androidx.lifecycle.*
import campus.tech.kakao.map.data.Keyword
import campus.tech.kakao.map.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _searchResults = MutableLiveData<List<Keyword>>()
    val searchResults: LiveData<List<Keyword>> = _searchResults

    private val _savedKeywords = MutableLiveData<List<Keyword>>()
    val savedKeywords: LiveData<List<Keyword>> = _savedKeywords

    private val _selectedKeyword = MutableLiveData<Keyword>()
    val selectedKeyword: LiveData<Keyword> = _selectedKeyword

    private val _lastMarker = MutableLiveData<Keyword>()
    val lastMarker: LiveData<Keyword> = _lastMarker

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _savedKeywords.postValue(repository.getAllSavedKeywordsFromPrefs())
                loadLastMarkerPosition()
            }
        }
    }

    fun search(query: String) {
        viewModelScope.launch {
            val results = withContext(Dispatchers.Default) {
                repository.search(query)
            }
            _searchResults.value = results
        }
    }

    fun saveKeyword(keyword: Keyword) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentSavedKeywords = _savedKeywords.value?.toMutableList() ?: mutableListOf()
                if (!currentSavedKeywords.contains(keyword)) {
                    currentSavedKeywords.add(0, keyword)
                    _savedKeywords.postValue(currentSavedKeywords)
                    repository.saveKeywordToPrefs(keyword)
                }
            }
        }
    }

    fun deleteKeyword(keyword: Keyword) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentSavedKeywords = _savedKeywords.value?.toMutableList() ?: mutableListOf()
                currentSavedKeywords.remove(keyword)
                _savedKeywords.postValue(currentSavedKeywords)
                repository.deleteKeywordFromPrefs(keyword)
            }
        }
    }

    fun processActivityResult(data: Intent) {
        val placeName = data.getStringExtra("place_name")
        val roadAddressName = data.getStringExtra("road_address_name")
        val x = data.getDoubleExtra("x", 0.0)
        val y = data.getDoubleExtra("y", 0.0)

        if (placeName != null && roadAddressName != null) {
            val keyword = Keyword(0, placeName, roadAddressName, x, y)
            _selectedKeyword.value = keyword
            saveLastMarkerPosition(keyword)
        }
    }

    fun saveLastMarkerPosition(keyword: Keyword) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.saveLastMarkerPosition(keyword.x, keyword.y, keyword.name, keyword.address)
            }
        }
    }

    fun loadLastMarkerPosition() {
        viewModelScope.launch {
            val keyword = withContext(Dispatchers.IO) {
                repository.loadLastMarkerPosition()
            }
            if (keyword != null) {
                _lastMarker.value = keyword
            }
        }
    }
}