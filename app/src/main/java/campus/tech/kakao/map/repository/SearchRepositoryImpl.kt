package campus.tech.kakao.map.repository

import campus.tech.kakao.map.ApiKey
import campus.tech.kakao.map.model.KakaoLocalService
import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.model.SavePlace
import campus.tech.kakao.map.model.SavePlaceDao
import campus.tech.kakao.map.model.SearchPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
    private val retrofit: KakaoLocalService,
    private val savePlaceDao: SavePlaceDao,
    @ApiKey private val apiKey: String
) : SearchRepository {

    override suspend fun savePlaces(placeName: String): List<SavePlace> {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(placeName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
            savePlaceDao.insert(SavePlace(savePlaceName = placeName))
        }
        return showSavePlace()
    }

    override suspend fun showSavePlace(): List<SavePlace> {
        return withContext(Dispatchers.IO) {
            savePlaceDao.getAll()
        }
    }

    override suspend fun deleteSavedPlace(savedPlaceName: String): List<SavePlace> {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(savedPlaceName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
        }
        return showSavePlace()
    }

    override fun getPlaceList(categoryGroupName: String, callback: (List<PlaceInfo>?) -> Unit) {
        retrofit.getPlaceList(apiKey, categoryGroupName)
            .enqueue(object : Callback<SearchPlace> {
                override fun onResponse(
                    call: Call<SearchPlace>,
                    response: Response<SearchPlace>
                ) {
                    callback(response.body()?.documents)
                }

                override fun onFailure(call: Call<SearchPlace>, t: Throwable) {
                    callback(null)
                }
            })
    }
}
