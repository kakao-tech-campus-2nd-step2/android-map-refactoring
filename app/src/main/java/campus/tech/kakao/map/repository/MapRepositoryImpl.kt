package campus.tech.kakao.map.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MapRepository {
    private val spf: SharedPreferences =
        context.getSharedPreferences("map_prefs", Context.MODE_PRIVATE)

    override fun saveLastPosition(latitude: Double, longitude: Double) {
        val editor = spf.edit()
        editor.putString("latitude", latitude.toString())
        editor.putString("longitude", longitude.toString())
        editor.apply()
    }

    override fun getLastPosition(): Pair<Double, Double> {
        val latitude = spf.getString("latitude", "37.394660")?.toDouble() ?: 37.394660
        val longitude = spf.getString("longitude", "127.111182")?.toDouble() ?: 127.111182
        return Pair(latitude, longitude)
    }
}