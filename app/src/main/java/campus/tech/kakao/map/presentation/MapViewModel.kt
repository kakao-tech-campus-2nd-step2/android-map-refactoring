package campus.tech.kakao.map.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.usecase.GetLastPlaceUseCase
import campus.tech.kakao.map.domain.usecase.SaveLastPlaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val saveLastPlaceUseCase: SaveLastPlaceUseCase,
    private val getLastPlaceUseCase: GetLastPlaceUseCase,
) : ViewModel() {

    private val _lastPlace = MutableLiveData<PlaceVO?>()
    val lastPlace: LiveData<PlaceVO?> get() = _lastPlace


    init {
        getLastPlace()
    }


    fun saveLastPlace(place: PlaceVO) {
        viewModelScope.launch(Dispatchers.IO) {
            saveLastPlaceUseCase(place)
            _lastPlace.postValue(place)
        }
    }

    private fun getLastPlace() {
        viewModelScope.launch(Dispatchers.IO) {
            _lastPlace.postValue(getLastPlaceUseCase())
        }
    }

    fun setLastPlace(place: Serializable) {
        _lastPlace.value = place as PlaceVO
    }
}