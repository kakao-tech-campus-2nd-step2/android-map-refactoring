package campus.tech.kakao.map.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.NetworkRepository
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.domain.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> get() = _placeList

    private val _searchList = MutableLiveData<List<Place>>()
    val searchList: LiveData<List<Place>> get() = _searchList

    private val _isPlaceListVisible = MutableLiveData<Boolean>()
    val isPlaceListVisible: LiveData<Boolean> get() = _isPlaceListVisible

    private val _isSearchListVisible = MutableLiveData<Boolean>()
    val isSearchListVisible: LiveData<Boolean> get() = _isSearchListVisible

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            val placeEntities = placeRepository.getAllPlaces()
            val places = placeEntities.map { it.toDomain() }
            _searchList.postValue(places)
            _isSearchListVisible.postValue(places.isNotEmpty())
        }
    }

    fun searchPlace(keyword: String) {
        viewModelScope.launch {
            networkRepository.searchPlace(keyword,
                onSuccess = { newKeywordList ->
                    _placeList.postValue(newKeywordList)
                    _isPlaceListVisible.postValue(newKeywordList.isNotEmpty())
                },
                onFailure = { throwable ->
                    Log.w("API response", "Failure: $throwable")
                }
            )
        }
    }

    fun addPlaceRecord(place: Place) {
        viewModelScope.launch {
            placeRepository.insertPlace(place.toEntity())
            val updatedList = _searchList.value.orEmpty() + place
            _searchList.postValue(updatedList)
            _isSearchListVisible.postValue(updatedList.isNotEmpty())
        }
    }

    fun removePlaceRecord(place: Place) {
        viewModelScope.launch {
            placeRepository.deletePlace(place.toEntity())
            val updatedList = _searchList.value.orEmpty() - place
            _searchList.postValue(updatedList)
            _isSearchListVisible.postValue(updatedList.isNotEmpty())
        }
    }
}