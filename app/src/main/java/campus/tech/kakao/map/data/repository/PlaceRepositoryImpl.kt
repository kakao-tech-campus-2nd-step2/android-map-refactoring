package campus.tech.kakao.map.data.repository

import android.util.Log
import campus.tech.kakao.map.data.local.db.SearchQueryDao
import campus.tech.kakao.map.data.local.db.VisitedPlaceDao
import campus.tech.kakao.map.data.local.entity.SearchQueryEntity
import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity
import campus.tech.kakao.map.data.remote.network.HttpService
import campus.tech.kakao.map.domain.model.PlaceVO
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepositoryImpl @Inject constructor(
    private val searchQueryDao: SearchQueryDao,
    private val visitedPlaceDao: VisitedPlaceDao,
    private val ioDispatcher: CoroutineDispatcher
) : PlaceRepository {
    override suspend fun searchPlaces(query: String): List<PlaceVO>? = withContext(Dispatchers.IO) {

        val response = HttpService.searchKeyword(query = query)
        response?.documents?.map {
                    PlaceVO(
                        placeName = it.placeName,
                        addressName = it.addressName,
                        categoryName = it.categoryGroupName,
                        latitude = it.y.toDouble(),
                        longitude = it.x.toDouble()
                    )
        }
    }


    override suspend fun saveSearchQuery(place: PlaceVO): Unit = withContext(ioDispatcher) {
        val queryEntity = SearchQueryEntity(query = place.placeName)
        searchQueryDao.insert(queryEntity)
    }

    override suspend fun getSearchHistory(): List<String> = withContext(ioDispatcher) {
        searchQueryDao.getAll().map { it.query }.reversed()
    }

    override suspend fun removeSearchQuery(query: String) = withContext(ioDispatcher) {
        val queryEntity = SearchQueryEntity(query = query)
        searchQueryDao.delete(queryEntity)
    }

    override suspend fun saveLastPlace(place: PlaceVO) = withContext(ioDispatcher) {
        val visitedPlaceEntity = VisitedPlaceEntity(
            placeName = place.placeName,
            addressName = place.addressName,
            categoryName = place.categoryName,
            latitude = place.latitude,
            longitude = place.longitude
        )
        visitedPlaceDao.insert(visitedPlaceEntity)
    }

    override suspend fun getLastPlace(): PlaceVO? = withContext(ioDispatcher) {
        val lastPlace = visitedPlaceDao.getLastPlace()
        lastPlace?.let {
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