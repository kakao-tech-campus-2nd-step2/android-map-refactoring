package campus.tech.kakao.map.repository

import android.content.SharedPreferences
import android.util.Log
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.data.remote.api.KakaoApiService
import campus.tech.kakao.map.data.remote.model.KakaoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PlaceRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val kakaoApiService: KakaoApiService
): PlaceRepositoryInterface {

    override suspend fun searchPlaces(query: String): List<Place>{
        val apiKey = "KakaoAK " + BuildConfig.KAKAO_REST_API_KEY

        return try {
            val response = kakaoApiService.getPlace(apiKey, query)
            val documentList = response.documents ?: emptyList()
            documentList.map {
                Place(
                    img = R.drawable.location,
                    name = it.placeName,
                    location = it.addressName,
                    category = it.categoryGroupName,
                    x = it.x,
                    y = it.y
                )
            }
        } catch (e: Exception) {
            Log.e("KakaoAPI", "Failure: ${e.message}")
            emptyList()
        }
    }

    override fun saveLastLocation(item: Place) {
        with(sharedPreferences.edit()) {
            putString("PLACE_X", item.x)
            putString("PLACE_Y", item.y)
            apply()
        }
    }

}