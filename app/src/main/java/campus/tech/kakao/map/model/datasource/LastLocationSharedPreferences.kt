package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.model.Location
import javax.inject.Inject

class LastLocationSharedPreferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun putLastLocation(location: Location) {
        sharedPreferences.putString("id", location.id.toString())
        sharedPreferences.putString("longitude", location.longitude.toString())
        sharedPreferences.putString("latitude", location.latitude.toString())
        sharedPreferences.putString("title", location.title.toString())
        sharedPreferences.putString("address", location.address.toString())
        sharedPreferences.putString("category", location.category.toString())
    }

    fun getLastLocation(): Location? {
        val id = sharedPreferences.getString("id", "").toString()
        if(id == "") return null
        val title = sharedPreferences.getString("title", "")
        val longitude = sharedPreferences.getString("longitude", "").toString().toDouble()
        val latitude = sharedPreferences.getString("latitude", "").toString().toDouble()
        val address = sharedPreferences.getString("address", "").toString()
        val category = sharedPreferences.getString("category", "").toString()
        return Location(id.toLong(), title, address, category, longitude, latitude)
    }
}