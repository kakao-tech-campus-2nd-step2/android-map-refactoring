package campus.tech.kakao.map.repository

import android.content.SharedPreferences
import android.util.Log
import campus.tech.kakao.map.data.KeywordDao
import campus.tech.kakao.map.data.KakaoLocalApiService
import campus.tech.kakao.map.data.Keyword
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val keywordDao: KeywordDao,
    private val kakaoApiService: KakaoLocalApiService
) : Repository {

    override suspend fun search(query: String): List<Keyword> = withContext(Dispatchers.IO) {
        val response = kakaoApiService.searchPlaces(query).execute()
        if (response.isSuccessful) {
            val responseBody = response.body()
            responseBody?.documents?.map {
                Keyword(it.id.toInt(), it.place_name, it.address_name, it.x.toDouble(), it.y.toDouble())
            } ?: emptyList()
        } else {
            Log.e("Repository", "Search API call failed: ${response.code()} ${response.message()}")
            emptyList()
        }
    }

    override suspend fun getAllSavedKeywordsFromPrefs(): List<Keyword> {
        val savedKeywordsJson = sharedPreferences.getString("saved_keywords", null) ?: return emptyList()
        val type = object : TypeToken<List<Keyword>>() {}.type
        return Gson().fromJson(savedKeywordsJson, type)
    }

    override suspend fun saveKeywordToPrefs(keyword: Keyword) = withContext(Dispatchers.IO) {
        val savedKeywords = getAllSavedKeywordsFromPrefs().toMutableList()
        savedKeywords.add(0, keyword)
        val editor = sharedPreferences.edit()
        editor.putString("saved_keywords", Gson().toJson(savedKeywords))
        editor.apply()
    }

    override suspend fun deleteKeywordFromPrefs(keyword: Keyword) = withContext(Dispatchers.IO) {
        val savedKeywords = getAllSavedKeywordsFromPrefs().toMutableList()
        savedKeywords.remove(keyword)
        val editor = sharedPreferences.edit()
        editor.putString("saved_keywords", Gson().toJson(savedKeywords))
        editor.apply()
    }

    override suspend fun saveLastMarkerPosition(latitude: Double, longitude: Double, placeName: String, roadAddressName: String) = withContext(Dispatchers.IO) {
        val sharedPreferences = sharedPreferences
        with(sharedPreferences.edit()) {
            putFloat(PREF_LATITUDE, latitude.toFloat())
            putFloat(PREF_LONGITUDE, longitude.toFloat())
            putString(PREF_PLACE_NAME, placeName)
            putString(PREF_ROAD_ADDRESS_NAME, roadAddressName)
            apply()
        }
    }

    override suspend fun loadLastMarkerPosition(): Keyword? = withContext(Dispatchers.IO) {
        val sharedPreferences = sharedPreferences
        if (sharedPreferences.contains(PREF_LATITUDE) && sharedPreferences.contains(PREF_LONGITUDE)) {
            val latitude = sharedPreferences.getFloat(PREF_LATITUDE, 0.0f).toDouble()
            val longitude = sharedPreferences.getFloat(PREF_LONGITUDE, 0.0f).toDouble()
            val placeName = sharedPreferences.getString(PREF_PLACE_NAME, "") ?: ""
            val roadAddressName = sharedPreferences.getString(PREF_ROAD_ADDRESS_NAME, "") ?: ""

            if (placeName.isNotEmpty() && roadAddressName.isNotEmpty()) {
                Keyword(0, placeName, roadAddressName, longitude, latitude)
            } else {
                null
            }
        } else {
            null
        }
    }

    companion object {
        private const val PREFS_NAME = "LastMarker"
        private const val PREF_LATITUDE = "lastX"
        private const val PREF_LONGITUDE = "lastY"
        private const val PREF_PLACE_NAME = "lastName"
        private const val PREF_ROAD_ADDRESS_NAME = "lastAdress"
    }
}
