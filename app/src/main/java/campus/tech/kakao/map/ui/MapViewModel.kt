package campus.tech.kakao.map.ui

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.domain.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MapViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @Named("latitude") private val preferencesLatitude: String,
    @Named("longitude") private val preferencesLongitude: String
) : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> get() = _address

    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> get() = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> get() = _longitude

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage


    init {
        _latitude.value = preferencesLatitude
        _longitude.value = preferencesLongitude
    }

    fun updateLocationFromIntent(place: Place) {
        _name.value = place.name
        _address.value = place.address
        _category.value = place.category ?: ""
        _latitude.value = place.x ?: preferencesLatitude
        _longitude.value = place.y ?: preferencesLongitude
    }

    fun onMapError(error: Exception) {
        _errorMessage.value = "지도 인증을 실패했습니다.\n 다시 시도해주세요.\n ${error.message}"
    }

    fun saveLocation() {
        sharedPreferences.edit().apply {
            putString("latitude", _latitude.value)
            putString("longitude", _longitude.value)
            apply()
        }
    }

}
