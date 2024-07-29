package campus.tech.kakao.map.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.NetworkRepository
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.RetrofitLocalKeywordService
import campus.tech.kakao.map.domain.Place
import campus.tech.kakao.map.domain.PlaceUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val networkRepository: NetworkRepository
) : ViewModel() {

    private val _uiModel = MutableLiveData<PlaceUiModel>()
    val uiModel: LiveData<PlaceUiModel> get() = _uiModel

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            val placeEntities = placeRepository.getAllPlaces()
            val places = placeEntities.map { it.toDomain() }
            _uiModel.value = PlaceUiModel(
                placeList = emptyList(),
                searchList = places,
                isPlaceListVisible = false,
                isSearchListVisible = places.isNotEmpty()
            )
        }
    }

    fun searchPlace(keyword: String) {
        viewModelScope.launch {
            networkRepository.searchPlaceByKeyword(keyword,
                onSuccess = { updatedPlaceList ->
                    _uiModel.value = _uiModel.value?.copy(
                        placeList = updatedPlaceList,
                        isPlaceListVisible = updatedPlaceList.isNotEmpty()
                    )
                }
            )
        }
    }

    fun addPlaceRecord(place: Place) {
        viewModelScope.launch {
            placeRepository.insertPlace(place.toEntity())
            val updatedPlaceList = _uiModel.value?.searchList.orEmpty() + place
            _uiModel.value = _uiModel.value?.copy(
                searchList = updatedPlaceList,
                isSearchListVisible = updatedPlaceList.isNotEmpty()
            )
        }
    }

    fun removePlaceRecord(place: Place) {
        viewModelScope.launch {
            placeRepository.deletePlace(place.toEntity())
            val updatedPlaceList = _uiModel.value?.searchList.orEmpty() - place
            _uiModel.value = _uiModel.value?.copy(
                searchList = updatedPlaceList,
                isSearchListVisible = updatedPlaceList.isNotEmpty()
            )
        }
    }
}