package campus.tech.kakao.map.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.health.connect.datatypes.ExerciseRoute
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import campus.tech.kakao.map.model.data.Location
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch
import campus.tech.kakao.map.model.database.DatabaseManager
import campus.tech.kakao.map.model.network.KakaoLocalService
import campus.tech.kakao.map.model.network.KakaoSearchResponse
import campus.tech.kakao.map.model.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyRepository(context: Context) {

    private val databaseManager = DatabaseManager(context)
    private val apiService: KakaoLocalService = RetrofitInstance.api
    private val sharedPreferences : SharedPreferences =context.getSharedPreferences(
        "PlacePreferences", AppCompatActivity.MODE_PRIVATE
    )
    private val editor = sharedPreferences.edit()

    //sharedPreferences 저장하기
    fun setSharedPreferences(location : Location){
        with(editor){
            putString("name", location.name)
            putString("address", location.address)
            putString("longitude", location.longitude.toString())
            putString("latitude", location.latitude.toString())
            apply()
        }
    }

    //Place item 클릭하면 호출
    fun insertSavedsearch(id: Int, name: String) {
        databaseManager.insertSavedsearch(id, name)
    }

    //SavedSearch 업데이트, 클릭하면 같이 호출
    fun getSavedSearches(): List<SavedSearch> {
        return databaseManager.getSavedSearches()
    }

    //close 누르면 호출, item 삭제
    fun deleteSavedSearch(id: Int) {
        databaseManager.deleteSavedSearch(id)
    }

    //검색어로 검색하기
    suspend fun searchKeyword(query: String): Response<KakaoSearchResponse> {
        return withContext(Dispatchers.IO) {
            apiService.searchKeyword(query).execute()
        }
    }


}