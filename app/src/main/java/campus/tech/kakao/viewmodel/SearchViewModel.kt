package campus.tech.kakao.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.model.network.KakaoApiService
import campus.tech.kakao.model.Place
import campus.tech.kakao.model.ResultSearchKeyword
import campus.tech.kakao.model.database.RoomDb
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val db: RoomDb,
    private val kakaoApiService: KakaoApiService
) : ViewModel() {

    private val liveSelectedData = MutableLiveData<List<Pair<Long, String>>>()
    val selectedData: LiveData<List<Pair<Long, String>>> get() = liveSelectedData

    private val liveSearchResults = MutableLiveData<List<Place>>()
    val searchResults: LiveData<List<Place>> get() = liveSearchResults

    private val selectedPlace = MutableLiveData<Place>()
    val place: LiveData<Place> get() = selectedPlace

    fun loadSelectedData() {
        viewModelScope.launch {
            liveSelectedData.value = db.getAllSelectedData()
        }
    }

    fun insertSelectedData(name: String) {
        viewModelScope.launch {
            db.insertIntoSelectedData(name)
            loadSelectedData()
        }
    }

    fun deleteSelectedData(id: Long) {
        viewModelScope.launch {
            db.deleteFromSelectedData(id)
            loadSelectedData()
        }
    }

    fun searchPlaces(apiKey: String, query: String) {
        val authHeader = "KakaoAK $apiKey"

        val call = kakaoApiService.searchPlaces(authHeader, query)
        call.enqueue(object : Callback<ResultSearchKeyword> {
            override fun onResponse(call: Call<ResultSearchKeyword>, response: Response<ResultSearchKeyword>) {
                if (response.isSuccessful && response.body() != null) {
                    liveSearchResults.value = response.body()?.documents ?: emptyList()
                } else { liveSearchResults.value = emptyList() }
            }

            override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) { liveSearchResults.value = emptyList() }
        })
    }

    fun selectPlace(place: Place) {
        selectedPlace.value = place
    }
}