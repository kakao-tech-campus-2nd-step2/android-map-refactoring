package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.SavedPlace
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.SavedPlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchActivityViewModel @Inject constructor(
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