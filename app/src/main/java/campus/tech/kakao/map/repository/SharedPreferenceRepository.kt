package campus.tech.kakao.map.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import campus.tech.kakao.map.view.MapActivity
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.map

class SharedPreferenceRepository(private val dataStore: DataStore<Preferences>) {

    companion object {
        val KEY_LATITUDE = doublePreferencesKey("latitude")
        val KEY_LONGITUDE = doublePreferencesKey("longitude")
    }

    suspend fun putPos(latitude : Double, longitude : Double){
        dataStore.edit { preferences ->
            preferences[KEY_LATITUDE] = latitude
            preferences[KEY_LONGITUDE] = longitude
        }
    }

    val pos = dataStore.data.map { preferences ->
        val latitude = preferences[KEY_LATITUDE] ?: MapActivity.LATITUDE.toDouble()
        val longitude = preferences[KEY_LONGITUDE] ?: MapActivity.LONGITUDE.toDouble()
        LatLng.from(latitude, longitude)
    }
}