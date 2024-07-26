package campus.tech.kakao.map.data

import campus.tech.kakao.map.api.KakaoApiDataSource
import campus.tech.kakao.map.data.Place
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val kakaoApiDataSource : KakaoApiDataSource){
    suspend fun getKakaoLocalPlaceData(text : String) : List<Place>{
        val placeList = kakaoApiDataSource.getPlaceData(text)
        return placeList
    }
}