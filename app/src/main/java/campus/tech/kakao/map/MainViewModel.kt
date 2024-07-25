package campus.tech.kakao.map

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()

    private val apiService = KakaoAPIRetrofitClient.retrofitService
    var repository = PlaceRepository(apiService)
    var preferencesRepository = PreferencesRepository(application.applicationContext)

    private val _searchResults = MutableLiveData<List<Document>>()
    val searchResults: LiveData<List<Document>> get() = _searchResults

    private val _savedSearches = MutableLiveData<List<String>>()
    val savedSearches: LiveData<List<String>> get() = _savedSearches

    init {
        loadSavedSearches()
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val results = withContext(Dispatchers.IO) {
                val response = apiService.getSearchKeyword(query)
                response.documents.map { document ->
                    Place(
                        name = document.place_name,
                        address = document.address_name,
                        category = document.category_group_name,
                        x = document.x,
                        y = document.y
                    )
                }
            }
            _searchResults.postValue(results)
        }
    }

    fun addSearch(name: String, address: String, category: String, x: String, y: String) {
        viewModelScope.launch {
            val place = Place(name = name, address = address, category = category, x = x, y = y)
            withContext(Dispatchers.IO) {
                placeDao.deletePlace(name, address, category)
                placeDao.insert(place)
            }
            loadSavedSearches()
        }
    }

    fun removeSearch(name: String, address: String, category: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                placeDao.deletePlace(name, address, category)
            }
            loadSavedSearches()
        }
    }

    fun loadSavedSearches() {
        viewModelScope.launch {
            val searches = withContext(Dispatchers.IO) {
                placeDao.searchDatabase("%")
            }
        }
        _savedSearches.postValue(searches.reversed)
    }

    /*
    private fun saveSearchesToPreferences(searches: List<String>) {
        preferencesRepository.saveSearches(searches)
    }*/

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }
}