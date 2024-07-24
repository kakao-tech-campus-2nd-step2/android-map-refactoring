package campus.tech.kakao.map.model.datasource

import campus.tech.kakao.map.App
import campus.tech.kakao.map.model.Location

class LastLocationlSharedPreferences() {

    fun putLastLocation(location: Location) {
        App.sharedPreferencesManager.putString("id", location.id.toString())
        App.sharedPreferencesManager.putString("longitude", location.longitude.toString())
        App.sharedPreferencesManager.putString("latitude", location.latitude.toString())
        App.sharedPreferencesManager.putString("title", location.title.toString())
        App.sharedPreferencesManager.putString("address", location.address.toString())
        App.sharedPreferencesManager.putString("category", location.category.toString())

    }

    fun getLastLocation(): Location? {
        val id = App.sharedPreferencesManager.getString("id", "").toString()
        if(id == "") return null
        val title = App.sharedPreferencesManager.getString("title", "")
        val longitude = App.sharedPreferencesManager.getString("longitude", "").toString().toDouble()
        val latitude = App.sharedPreferencesManager.getString("latitude", "").toString().toDouble()
        val address = App.sharedPreferencesManager.getString("address", "").toString()
        val category = App.sharedPreferencesManager.getString("category", "").toString()
        return Location(id.toLong(), title, address, category, longitude, latitude)
    }
}