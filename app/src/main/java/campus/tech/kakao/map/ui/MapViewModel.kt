package campus.tech.kakao.map.ui

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> get() = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> get() = _longitude

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _hasIntentData = MutableLiveData<Boolean>()
    val hasIntentData: LiveData<Boolean> get() = _hasIntentData

    init {
        _latitude.value = preferencesLatitude
        _longitude.value = preferencesLongitude
        _hasIntentData.value = false
    }

    fun updateLocationFromIntent(intent: Intent) {
        _name.value = intent.getStringExtra("name")
        _address.value = intent.getStringExtra("address")
        _latitude.value = intent.getStringExtra("latitude") ?: preferencesLatitude
        _longitude.value = intent.getStringExtra("longitude") ?: preferencesLongitude

        _hasIntentData.value = !(_name.value.isNullOrEmpty() || _address.value.isNullOrEmpty() || _latitude.value.isNullOrEmpty() || _longitude.value.isNullOrEmpty())

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

    fun logSavedLocation() {
        val latitude = sharedPreferences.getString("latitude", "Not Found")
        val longitude = sharedPreferences.getString("longitude", "Not Found")
        Log.d("MapViewModel", "Saved latitude: $latitude")
        Log.d("MapViewModel", "Saved longitude: $longitude")
    }

}