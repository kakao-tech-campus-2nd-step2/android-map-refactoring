package campus.tech.kakao.map.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.data.Place
import campus.tech.kakao.map.model.data.SavedSearch
import campus.tech.kakao.map.model.data.toLocation
import campus.tech.kakao.map.model.database.DatabaseManager
import campus.tech.kakao.map.model.network.KakaoSearchResponse
import campus.tech.kakao.map.model.network.RetrofitInstance
import campus.tech.kakao.map.model.repository.MyRepository
import campus.tech.kakao.map.view.PlaceAdapter
import campus.tech.kakao.map.view.SavedSearchAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val application : Application, private val repository: MyRepository) : AndroidViewModel(application) {

    val searchText = MutableLiveData<String>()  //검색어를 입력하는 editText
    var isIntent: MutableLiveData<Boolean> = MutableLiveData(false) //위치한 액티비티를 나타내는 변수
    var placeAdapterUpdateData = MutableLiveData<List<Place>>() //업데이트 해야하는 PlaceAdapter List<Place>
    var savedSearchAdapterUpdateData = MutableLiveData<List<SavedSearch>>() //업데이트 해야하는 SavedSearchAdapter List<SavedSearch)
    var itemClick = MutableLiveData<Place>() //Place의 item
    var nameClick = MutableLiveData<SavedSearch>() //savedSearch의 이름 부분
    var closeClick = MutableLiveData<SavedSearch>() // savedSearch의 x부분
//    private val _searchResults = MutableLiveData<List<Place>>()


    //PlaceAdapter 초기화
    val vmPlaceAdapter: PlaceAdapter = PlaceAdapter(listOf()) { place ->  //리사이클러뷰의 아이템을 누르면
        repository.insertSavedsearch(place.id, place.name)  //SavedSearch에 item 추가
        updateSavedSearch() //SavedSearch Ui업데이트
        repository.setSharedPreferences(place.toLocation()) //sharedPreference에 카메라 이동할 정보 저장
        itemClick.value = place //액티비티 이동하기 위한 전달
    }

    //SavedSearchAdapter 초기화
    val vmSavedSearchAdapter: SavedSearchAdapter = SavedSearchAdapter(listOf(),
        onCloseClick = { SavedSearch -> //SavedSearch의 x를 누르면
            repository.deleteSavedSearch(SavedSearch.id)  //SavedSearch item 삭제
            updateSavedSearch() //Savedsearch Ui업데이트
        },
        onNameClick = { SavedSearch ->   //SavedSearch의 이름을 누르면
            nameClick.value = SavedSearch   //화면에 보이는 text 설정
            searchText.value = SavedSearch.name //검색 쿼리
        }
    )

    //SharedPreferences에 item의 이름, 주소, 위도, 경도 저장하기
    fun setSharedPreferences(place: Place) {
        //sharedPreference를 이용해서 name,address,latitude,longitude 저장하기
        val sharedPreferences =context.getSharedPreferences(
            "PlacePreferences", AppCompatActivity.MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        editor.putString("name", place.name)
        editor.putString("address", place.address)
        editor.putString("longitude", place.longitude)
        editor.putString("latitude", place.latitude)
        editor.apply()
    }


    // true일 때 SearchPlaceActivity에 위치하고있음
    fun intentSearchPlace() {
        isIntent.value = true
    }

    //editText를 지우는 closeIcon 클릭이벤트
    fun clickCloseIcon() {
        //햅틱 진동 기능 추가하고 싶다..
        searchText.value = " " //editText빈칸으로 만들기
    }

    //(비동기) 카카오 키워드 검색, 검색 결과는 placeAdapterUpdateData에 List<Place>로 저장
    fun searchPlace(query: String) {
        viewModelScope.launch {
            try {
                val response: Response<KakaoSearchResponse> = repository.searchKeyword(query)
                if (response.isSuccessful) {
                    placeAdapterUpdateData.value = response.body()?.documents?.map { document ->
                        Place(
                            id = document.id.toInt(),
                            name = document.place_name,
                            address = document.address_name,
                            kind = document.category_name,
                            longitude = document.x, //경도
                            latitude = document.y   //위도
                        )
                    } ?: emptyList()
                } else {
                    Log.e("MyViewModel", "Search failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MyViewModel", "Exception in searchPlace", e)
            }
        }
    }



    //Repository에서 List(SavedSearch) 가져와서  savedSearchAdapterUpdateData에 저장
    fun updateSavedSearch() {
        savedSearchAdapterUpdateData.value = repository.getSavedSearches()
    }


}