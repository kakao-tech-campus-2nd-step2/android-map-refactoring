package campus.tech.kakao.map.model.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.model.data.KAKAO_LATITUDE
import campus.tech.kakao.map.model.data.KAKAO_LONGITUDE
import campus.tech.kakao.map.model.data.Location
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch
import campus.tech.kakao.map.model.database.AppDatabase
import campus.tech.kakao.map.model.network.KakaoLocalService
import campus.tech.kakao.map.model.network.KakaoSearchResponse
import campus.tech.kakao.map.model.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyRepository(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val savedSearchDao = database.savedSearchDao()
    private val placeDao = database.placeDao()

    private val apiService: KakaoLocalService = RetrofitInstance.api
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "PlacePreferences", AppCompatActivity.MODE_PRIVATE
    )
    private val editor = sharedPreferences.edit()
    val SavedSearchesData = MutableLiveData<List<SavedSearch>>()

    // SharedPreferences 저장하기
    fun setSharedPreferences(location: Location) {
        with(editor) {
            putString("name", location.name)
            putString("address", location.address)
            putString("latitude", location.latitude.toString())
            putString("longitude", location.longitude.toString())
            apply()
        }
    }

    // SharedPreferences 값 불러오기
    suspend fun getSharedPreferences(): Location = withContext(Dispatchers.IO) {
        Location(
            sharedPreferences.getString("name", "") ?: "",
            sharedPreferences.getString("address", "") ?: "",
            sharedPreferences.getString("latitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LATITUDE,
            sharedPreferences.getString("longitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LONGITUDE
        )
    }

    // Place item 클릭하면 호출
    suspend fun insertSavedSearch(savedSearch: SavedSearch) {
        withContext(Dispatchers.IO) {
            savedSearchDao.insert(savedSearch)
        }
    }

    // SavedSearch 목록 가져오기
    suspend fun getSavedSearches(): List<SavedSearch> {
        val result = withContext(Dispatchers.IO) {
            savedSearchDao.getAll()
        }

        return result.value ?: listOf<SavedSearch>()
    }



    // close 누르면 호출, item 삭제
    suspend fun deleteSavedSearch(id: Int) {
        withContext(Dispatchers.IO) {
            val savedSearch = SavedSearch(id = id, name = "")
            savedSearchDao.delete(savedSearch)
        }
    }

    // 검색어로 검색하기
    suspend fun searchKeyword(query: String): Response<KakaoSearchResponse> {
        return withContext(Dispatchers.IO) {
            apiService.searchKeyword(query).execute()
        }
    }
}
