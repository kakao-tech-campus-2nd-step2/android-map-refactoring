package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.repository.PlaceRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepositoryInterface,
): ViewModel() {

    private val _placeList = MutableLiveData<List<Place>>()
    val placeList: LiveData<List<Place>> get() = _placeList

    private var _placeListVisible = MutableLiveData<Boolean>()
    val placeListVisible: LiveData<Boolean> get() = _placeListVisible

    fun callResultList(userInput: String){
        viewModelScope.launch {
            if (userInput.isBlank()) {
                _placeList.value = emptyList()
            } else {
                val result = placeRepository.searchPlaces(userInput)
                _placeList.value = result
                updatePlaceListVisibility()
            }
        }
    }

    private fun updatePlaceListVisibility(){
        _placeListVisible.value = !_placeList.value.isNullOrEmpty()
    }

    fun clearPlaceList() {
        _placeList.postValue(emptyList())
    }

    fun saveLastLocation(item: Place) {
        placeRepository.saveLastLocation(item)
    }
}