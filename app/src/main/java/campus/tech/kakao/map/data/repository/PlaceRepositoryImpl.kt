package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.local.db.SearchQueryDao
import campus.tech.kakao.map.data.local.db.VisitedPlaceDao
import campus.tech.kakao.map.data.local.entity.SearchQueryEntity
import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity
import campus.tech.kakao.map.data.remote.network.HttpService
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val searchQueryDao: SearchQueryDao,
    private val visitedPlaceDao: VisitedPlaceDao
) : PlaceRepository {
    override fun searchPlaces(query: String, callback: (List<PlaceVO>?) -> Unit) {
        HttpService.searchKeyword(query = query) { response ->
            if (response != null) {
                val places = response?.documents?.map {
                    PlaceVO(
                        placeName = it.placeName,
                        addressName = it.addressName,
                        categoryName = it.categoryGroupName,
                        latitude = it.y.toDouble(),
                        longitude = it.x.toDouble()
                    )
                }
                callback(places)
            } else {
                Log.d("testt", "Response failed: ${response.toString()}")
                callback(null)
            }
        }
    }


    override fun saveSearchQuery(place: PlaceVO) {
        val queryEntity = SearchQueryEntity(query = place.placeName)
        searchQueryDao.insert(queryEntity)
    }

    override fun getSearchHistory(): List<String> {
        return searchQueryDao.getAll().map { it.query }.reversed()
    }

    override fun removeSearchQuery(query: String) {
        val queryEntity = SearchQueryEntity(query = query)
        searchQueryDao.delete(queryEntity)
    }

    override fun saveLastPlace(place: PlaceVO) {
        val visitedPlaceEntity = VisitedPlaceEntity(
            placeName = place.placeName,
            addressName = place.addressName,
            categoryName = place.categoryName,
            latitude = place.latitude,
            longitude = place.longitude
        )
        visitedPlaceDao.insert(visitedPlaceEntity)
    }

    override fun getLastPlace(): PlaceVO? {
        val lastPlace = visitedPlaceDao.getLastPlace()
        return lastPlace?.let {
            PlaceVO(
                placeName = it.placeName,
                addressName = it.addressName,
                categoryName = it.categoryName,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }
}