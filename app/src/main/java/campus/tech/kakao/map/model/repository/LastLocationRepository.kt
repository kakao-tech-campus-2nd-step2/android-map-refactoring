package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LastLocationlSharedPreferences

class LastLocationRepository(
    private val lastLocationlSharedPreferences: LastLocationlSharedPreferences
) {
    fun putLastLocation(location: Location){
        lastLocationlSharedPreferences.putLastLocation(location)
    }

    fun getLastLocation(): Location? {
        return lastLocationlSharedPreferences.getLastLocation()
    }
}