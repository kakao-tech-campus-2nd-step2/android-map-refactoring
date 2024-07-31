package campus.tech.kakao.map.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import campus.tech.kakao.map.utilities.Constants
import campus.tech.kakao.map.view.MapActivity
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PositionDataSource @Inject constructor(private val dataStore: DataStore<Preferences>) {

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
        val latitude = preferences[KEY_LATITUDE] ?: Constants.ChonnamUnivLocation.LATITUDE.toDouble()
        val longitude = preferences[KEY_LONGITUDE] ?: Constants.ChonnamUnivLocation.LONGITUDE.toDouble()
        LatLng.from(latitude, longitude)
    }
}