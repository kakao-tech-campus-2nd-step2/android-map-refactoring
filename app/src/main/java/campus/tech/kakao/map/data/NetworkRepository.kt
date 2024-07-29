package campus.tech.kakao.map.data

import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.domain.Place
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val retrofitLocalKeywordService: RetrofitLocalKeywordService) {

    suspend fun searchPlaceByKeyword(keyword: String, onSuccess: (List<Place>) -> Unit) {
        val response = retrofitLocalKeywordService.searchPlaceByKeyword("KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}", keyword)
        if (response.isSuccessful) {
            val keywordList: MutableList<Place> = mutableListOf()
            val places = response.body()?.documents
            places?.let {
                for (placeInfo in places) {
                    val place = Place(placeInfo.placeName, placeInfo.addressName, placeInfo.categoryGroupName, placeInfo.x, placeInfo.y)
                    keywordList.add(place)
                }
            }
            onSuccess(keywordList)
            Log.d("API response", "Success: $places")
        }
        else {
            val errorBody = response.errorBody()?.string()
            Log.d("API response", "Error response: $errorBody")
        }
    }
}
