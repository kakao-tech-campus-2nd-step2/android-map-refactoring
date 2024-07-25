package campus.tech.kakao.map.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.model.KakaoMapProductResponse
import campus.tech.kakao.map.model.MapItem
import campus.tech.kakao.map.model.toEntity
import campus.tech.kakao.map.network.RetrofitClient
import campus.tech.kakao.map.repository.MapItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    application: Application,
    private var repository: MapItemRepository
) : AndroidViewModel(application) {

    private val _searchResults = MutableLiveData<List<MapItem>>()
    val searchResults: LiveData<List<MapItem>> get() = _searchResults

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun searchPlaces(keyword: String) {
        val apiKey = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}"
        RetrofitClient.apiService.searchPlaces(apiKey, keyword).enqueue(object :
            Callback<KakaoMapProductResponse> {
            override fun onResponse(call: Call<KakaoMapProductResponse>, response: Response<KakaoMapProductResponse>) {
                if (response.isSuccessful) {
                    val documents = response.body()?.documents ?: emptyList()
                    val results = documents.map { MapItem(it.place_name, it.address_name, it.category_group_name, it.x.toDouble(), it.y.toDouble()) }
                    _searchResults.postValue(results)
                    saveResultsToDatabase(results)
                } else {
                    _searchResults.postValue(emptyList())
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<KakaoMapProductResponse>, t: Throwable) {
                _searchResults.postValue(emptyList())
                _errorMessage.postValue("네트워크 요청 실패: ${t.message}")
            }
        })
    }

    private fun saveResultsToDatabase(results: List<MapItem>) {
        viewModelScope.launch {
            repository.deleteAll()
            val entities = results.map { it.toEntity() }
            repository.insertAll(entities)
        }
    }

    fun getSavedMapItems() {
        viewModelScope.launch {
            _searchResults.postValue(repository.getAllMapItems().map {
                MapItem(it.name, it.address, it.category, it.longitude, it.latitude)
            })
        }
    }

    fun clearMapItems() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }
}