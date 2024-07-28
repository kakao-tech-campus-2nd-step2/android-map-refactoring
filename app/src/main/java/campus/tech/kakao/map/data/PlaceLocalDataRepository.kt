package campus.tech.kakao.map.data

import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import javax.inject.Inject

open class PlaceLocalDataRepository @Inject constructor(
    private val placeDao: PlaceDao,
) : PlaceRepository {

    override suspend fun getPlaces(placeName: String): List<Place> {
        return placeDao.getPlaces(placeName).map { it.toPlace() }
    }

    override suspend fun updatePlaces(places: List<Place>) {
        placeDao.deleteAllPlaces()
        placeDao.insertPlaces(places.map {
            PlaceEntity(it.id, it.place, it.address, it.category, it.xPos, it.yPos)
        })
    }

    override suspend fun getPlaceById(id: String): Place? {
        return placeDao.getPlaceById(id)?.toPlace()
    }

    override suspend fun updateLogs(logs: List<Place>) {
        placeDao.deleteAllLogs()
        placeDao.insertLogs(logs.map { PlaceLogEntity(it.id, it.place) })
    }

    override suspend fun removeLog(id: String) {
        placeDao.removeLog(id)

    }

    override suspend fun getLogs(): List<Place> {
        return placeDao.getLogs().map { it.toPlace() }
    }
}
