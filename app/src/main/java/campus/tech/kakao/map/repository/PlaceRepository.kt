package campus.tech.kakao.map.repository

import campus.tech.kakao.map.model.Place
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val kakaoApiDataSource : KakaoApiDataSource){
    suspend fun getKakaoLocalPlaceData(text : String) : List<Place>{
        val placeList = kakaoApiDataSource.getPlaceData(text)
        return placeList
    }
}