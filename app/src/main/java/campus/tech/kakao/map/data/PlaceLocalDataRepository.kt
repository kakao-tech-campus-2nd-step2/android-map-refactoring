package campus.tech.kakao.map.data

import campus.tech.kakao.map.data.dao.PlaceDao
import campus.tech.kakao.map.data.entity.PlaceEntity
import campus.tech.kakao.map.data.entity.PlaceLogEntity
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class PlaceLocalDataRepository @Inject constructor(
    private val placeDao: PlaceDao,
) : PlaceRepository {

    override suspend fun getPlaces(placeName: String): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.getPlaces(placeName).map {
                Place(it.id, it.place, it.address, it.type, it.xPos, it.yPos)
            }
        }
    }

    override suspend fun updatePlaces(places: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllPlaces()
            placeDao.insertPlaces(places.map {
                PlaceEntity(it.id, it.place, it.address, it.category, it.xPos, it.yPos)
            })
        }
    }

    override suspend fun getPlaceById(id: String): Place? {
        return withContext(Dispatchers.IO) {
            placeDao.getPlaceById(id)?.let {
                Place(it.id, it.place, it.address, it.type, it.xPos, it.yPos)
            }
        }
    }

    override suspend fun updateLogs(logs: List<Place>) {
        withContext(Dispatchers.IO) {
            placeDao.deleteAllLogs()
            placeDao.insertLogs(logs.map {
                PlaceLogEntity(it.id, it.place)
            })
        }
    }

    override suspend fun removeLog(id: String) {
        withContext(Dispatchers.IO) {
            placeDao.removeLog(id)
        }
    }

    override suspend fun getLogs(): List<Place> {
        return withContext(Dispatchers.IO) {
            placeDao.getLogs().map {
                Place(it.id, it.place, "", "", "", "")
            }
        }
    }
}
