package campus.tech.kakao.map.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.Document
import campus.tech.kakao.map.model.PlaceResponse
import campus.tech.kakao.map.model.RetrofitService
import campus.tech.kakao.map.view.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel @Inject constructor(
    private val retrofitService: RetrofitService,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    /* SearchActivity */
    companion object { private const val API_KEY = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}" }

    private val _places = MutableLiveData<List<Document>>()
    private val _savedQueries = MutableLiveData<MutableList<String>>()
    val places: LiveData<List<Document>> get() = _places
    val savedQueries: LiveData<MutableList<String>> get() = _savedQueries

    init { _savedQueries.value = loadSavedQueries() }

    fun loadPlaces(query: String, categoryGroupName: String) {
        searchPlaces(query, categoryGroupName) { places ->
            _places.value = places
        }
    }

    fun addSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList().apply { add(query) }
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }

    fun removeSavedQuery(query: String) {
        val updatedList = _savedQueries.value.orEmpty().toMutableList().apply { remove(query) }
        _savedQueries.value = updatedList
        saveQueries(updatedList)
    }

    private fun loadSavedQueries(): MutableList<String> {
        val savedQueriesString = sharedPreferences.getString("queries", null)
        return if (!savedQueriesString.isNullOrEmpty()) {
            savedQueriesString.split(",").toMutableList()
        } else {
            mutableListOf()
        }
    }

    private fun saveQueries(queries: MutableList<String>) {
        sharedPreferences.edit().apply {
            putString("queries", queries.joinToString(","))
            apply()
        }
    }

    private fun searchPlaces(query: String, categoryGroupName: String, callback: (List<Document>) -> Unit) {
        retrofitService.getPlaces(API_KEY, categoryGroupName, query).enqueue(object :
            Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    val places = response.body()?.documents ?: emptyList()
                    callback(places)
                } else {
                    Log.e("PlaceViewModel", "Failed to fetch places: ${response.errorBody()?.string()}")
                    callback(emptyList())
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.e("PlaceViewModel", "Failed to fetch places: ${t.message}")
                callback(emptyList())
            }
        })
    }


    /* MainActivity */
    private val _placeName = MutableLiveData<String>()
    private val _addressName = MutableLiveData<String>()
    private val _longitude = MutableLiveData<Double>()
    private val _latitude = MutableLiveData<Double>()

    val placeName: LiveData<String> get() = _placeName
    val addressName: LiveData<String> get() = _addressName
    val longitude: LiveData<Double> get() = _longitude
    val latitude: LiveData<Double> get() = _latitude


    fun loadPlacePreferences(sharedPreferences: SharedPreferences) {
        _placeName.value = sharedPreferences.getString(MainActivity.EXTRA_PLACE_NAME, "Unknown Place")
        _addressName.value = sharedPreferences.getString(MainActivity.EXTRA_PLACE_ADDRESSNAME, "Unknown Address")
        _longitude.value = sharedPreferences.getString(MainActivity.EXTRA_PLACE_LONGITUDE, "127.108621")?.toDouble() ?: 0.0
        _latitude.value = sharedPreferences.getString(MainActivity.EXTRA_PLACE_LATITUDE, "37.402005")?.toDouble() ?: 0.0
    }

}