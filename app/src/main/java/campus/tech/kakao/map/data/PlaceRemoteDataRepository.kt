package campus.tech.kakao.map.data

import android.content.Context
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.domain.model.Place

class PlaceRemoteDataRepository(context: Context) : PlaceLocalDataRepository(context){
    override suspend fun getPlaces(placeName: String): List<Place> {
        val places = KakaoApiClient.getPlaces(placeName)
        updatePlaces(places)
        return places
    }
}
