package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.model.Place
import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

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