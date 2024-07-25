package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.repository.PlaceRepository
import campus.tech.kakao.map.repository.SavedPlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SearchActivityViewModel(
    private val placeRepository: PlaceRepository,
    private val savedPlaceRepository: SavedPlaceRepository
) : ViewModel() {
    private val _place = MutableLiveData<List<Place>>()
    private val _savedPlace = MutableLiveData<List<SavedPlace>>()
    val place: LiveData<List<Place>> get() = _place
    val savedPlace: LiveData<List<SavedPlace>> get() = _savedPlace

    init {
        getSavedPlace()
    }

    fun getPlace() {
        _place.value = (placeRepository.getAllPlace())
    }

    fun getPlaceWithCategory(category: String) {
        _place.value = (placeRepository.getPlaceWithCategory(category))
    }

    fun getSavedPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            _savedPlace.postValue(savedPlaceRepository.getAllSavedPlace())
        }
    }

    fun savePlace(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            savedPlaceRepository.writePlace(place)
            getSavedPlace()
        }
    }

    fun deleteSavedPlace(savedPlace: SavedPlace) {
        viewModelScope.launch(Dispatchers.IO){
            savedPlaceRepository.deleteSavedPlace(savedPlace)
            getSavedPlace()
        }
    }


    suspend fun getKakaoLocalData(text: String) {
        if (text.isNotEmpty()) {
            val placeList = placeRepository.getKakaoLocalPlaceData(text)
            _place.value = (placeList)
        } else _place.value = listOf<Place>()
    }
}