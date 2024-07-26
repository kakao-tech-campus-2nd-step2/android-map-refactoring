package campus.tech.kakao.map.repository.map.datasource

import android.content.SharedPreferences

class MapDataSourceImpl(private val preference: SharedPreferences) : MapDataSource {
    override fun updateLastPosition(latitude: Double, longitude: Double) {
        preference.edit().putString(KEY_LAST_LATITUDE, latitude.toString()).apply()
        preference.edit().putString(KEY_LAST_LONGITUDE, longitude.toString()).apply()
    }

    override fun readLastPosition(): Pair<Double?, Double?> {
        val latitude = preference.getString(KEY_LAST_LATITUDE, null)?.toDoubleOrNull()
        val longitude = preference.getString(KEY_LAST_LONGITUDE, null)?.toDoubleOrNull()
        return Pair(latitude, longitude)
    }
}
