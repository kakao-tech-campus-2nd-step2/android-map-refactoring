package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.model.Place
import javax.inject.Inject
import javax.inject.Singleton

class PlaceRepository @Inject constructor(private val kakaoApiDataSource : KakaoApiDataSource){
    suspend fun getKakaoLocalPlaceData(text : String) : List<Place>{
        val placeList = kakaoApiDataSource.getPlaceData(text)
        return placeList
    }
}