package campus.tech.kakao.map.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.model.SavePlace
import campus.tech.kakao.map.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    application: Application,
    private val searchRepo: SearchRepository
) : AndroidViewModel(application) {
    private val _places: MutableLiveData<List<PlaceInfo>> = MutableLiveData()
    private val _savePlaces: MutableLiveData<List<SavePlace>> = MutableLiveData()

    val places: LiveData<List<PlaceInfo>> get() = _places
    val savePlaces: LiveData<List<SavePlace>> get() = _savePlaces

    init {
        viewModelScope.launch {
            _savePlaces.value = searchRepo.showSavePlace()
        }
    }

    fun savePlaces(placeName: String) {
        viewModelScope.launch {
            _savePlaces.value = searchRepo.savePlaces(placeName)
        }
    }

    fun deleteSavedPlace(savedPlaceName: String) {
        viewModelScope.launch {
            _savePlaces.value = searchRepo.deleteSavedPlace(savedPlaceName)
        }
    }

    fun getPlaceList(categoryGroupName: String) {
        searchRepo.getPlaceList(categoryGroupName) { places ->
            _places.postValue(places)
        }
    }
}