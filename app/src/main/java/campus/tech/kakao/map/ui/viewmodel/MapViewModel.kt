package campus.tech.kakao.map.ui.viewmodel

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _latitude = MutableLiveData<String>()
    val latitude: LiveData<String> get() = _latitude

    private val _longitude = MutableLiveData<String>()
    val longitude: LiveData<String> get() = _longitude

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private val _showBottomSheet = MutableLiveData<Boolean>()
    val showBottomSheet: LiveData<Boolean> get() = _showBottomSheet

    init {
        initializeLatLng()
    }

    private fun initializeLatLng() {
        val lastLat = sharedPreferences.getString("lastLat", "35.231627") ?: "35.231627"
        val lastLng = sharedPreferences.getString("lastLng", "129.084020") ?: "129.084020"
        _latitude.value = lastLat
        _longitude.value = lastLng
    }

    fun setShowBottomSheet(show: Boolean) {
        Log.d("setShowBottomSheet", "I'm executed")
        _showBottomSheet.value = show
    }

    fun loadIntentData(intent: Intent) {
        _latitude.value = intent.getStringExtra("mapY") ?: _latitude.value
        _longitude.value = intent.getStringExtra("mapX") ?: _longitude.value
        _name.value = intent.getStringExtra("name")
        _address.value = intent.getStringExtra("address")
        Log.d("intent", "loadIntentData: ${_latitude.value}, ${_longitude.value}, ${_name.value}, ${_address.value}")
    }

    fun saveLatLng() {
        sharedPreferences.edit().apply {
            putString("lastLat", _latitude.value)
            putString("lastLng", _longitude.value)
            apply()
        }
    }
}
