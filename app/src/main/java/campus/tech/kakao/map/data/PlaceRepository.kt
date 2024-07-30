package campus.tech.kakao.map.data

import campus.tech.kakao.map.api.KakaoApiDataSource
import campus.tech.kakao.map.data.Place
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlaceRepository @Inject constructor(private val kakaoApiDataSource : KakaoApiDataSource, private val positionDataSource : PositionDataSource){
    suspend fun getKakaoLocalPlaceData(text : String) : List<Place>{
        val placeList = kakaoApiDataSource.getPlaceData(text)
        return placeList
    }

    suspend fun saveCurrentPosToDataStore(latitude : Double, longitude : Double){
        positionDataSource.putPos(latitude, longitude)
    }

    suspend fun fetchLastPosFromDataStore() : LatLng{
        val lastPos = positionDataSource.pos.first()
        return lastPos
    }

}