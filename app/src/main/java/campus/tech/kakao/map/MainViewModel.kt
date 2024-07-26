package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val placeRepository: PlaceRepository) : AndroidViewModel(application) {

    private val placeDao: PlaceDao = AppDatabase.getDatabase(application).placeDao()

    private val apiService = KakaoAPIRetrofitClient.retrofitService
    //var repository = PlaceRepository(apiService)
    //var preferencesRepository = PreferencesRepository(application.applicationContext)

    private val _searchResults = MutableLiveData<List<Place>>()
    val searchResults: LiveData<List<Place>> get() = _searchResults

    private val _savedSearches = MutableLiveData<List<Place>>()
    val savedSearches: LiveData<List<Place>> get() = _savedSearches

    init {
        loadSavedSearches()
    }

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val results = placeRepository.searchPlaces(query).map {document ->
                    Place(
                        name = document.place_name,
                        address = document.address_name,
                        category = document.category_group_name,
                        x = document.x,
                        y = document.y
                    )
                }
            _searchResults.postValue(results)
        }
    }

    fun addSearch(name: String, address: String, category: String, x: String, y: String) {
        viewModelScope.launch {
            val place = Place(name = name, address = address, category = category, x = x, y = y)
            placeRepository.savePlace(place)
            loadSavedSearches()
        }
    }

    fun removeSearch(name: String, address: String, category: String) {
        viewModelScope.launch {
            placeRepository.deletePlace(name, address, category)
            loadSavedSearches()
        }
    }

    fun loadSavedSearches() {
        viewModelScope.launch {
            val searches = placeRepository.getSavedPlaces()
            _savedSearches.postValue(searches.reversed())
        }
    }

    fun searchSavedPlace(savedQuery: String) {
        searchPlaces(savedQuery)
    }
}
