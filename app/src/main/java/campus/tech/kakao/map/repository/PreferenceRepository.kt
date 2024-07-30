package campus.tech.kakao.map.repository

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private var sharedPrefs: SharedPreferences =
        context.getSharedPreferences("location_data", Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    fun getString(key: String, value: String?): String {
        return sharedPrefs.getString(key, value).toString()
    }
}