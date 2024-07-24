package campus.tech.kakao.map.data

import android.content.Context
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.net.KakaoApi
import campus.tech.kakao.map.domain.model.Place
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRemoteDataRepository @Inject constructor(
    private val placeDao: PlaceDao,
    private val kakaoApi: KakaoApi
) : PlaceLocalDataRepository(placeDao){
    override suspend fun getPlaces(placeName: String): List<Place> {
        return withContext(Dispatchers.IO) {
            val resultPlaces = mutableListOf<Place>()
            for (page in 1..3) {
                val response = kakaoApi.getSearchKeyword(
                    key = BuildConfig.KAKAO_REST_API_KEY,
                    query = placeName,
                    size = 15,
                    page = page
                )
                if (response.isSuccessful) {
                    response.body()?.documents?.let { resultPlaces.addAll(it) }
                } else throw RuntimeException("통신 에러 발생")
            }
            updatePlaces(resultPlaces)
            resultPlaces
        }
    }
}
