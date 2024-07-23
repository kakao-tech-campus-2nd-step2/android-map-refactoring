package campus.tech.kakao.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor() : ViewModel() {

    fun saveLastLocation(context: Context, lat: Double, lng: Double) {
        viewModelScope.launch {
            val sharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putFloat("lastX", lng.toFloat())
                putFloat("lastY", lat.toFloat())
                apply()
            }
        }
    }

    fun getLastLocation(context: Context): Pair<Double, Double> {
        val sharedPreferences = context.getSharedPreferences("MapPrefs", Context.MODE_PRIVATE)
        val lat = sharedPreferences.getFloat("lastY", 37.402005f).toDouble()
        val lng = sharedPreferences.getFloat("lastX", 127.108621f).toDouble()
        return Pair(lat, lng)
    }
}