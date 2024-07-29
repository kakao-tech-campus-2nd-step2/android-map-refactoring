package campus.tech.kakao.map.data

import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDao: PlaceDao) {
    suspend fun insertPlace(place: PlaceEntity) {
        return placeDao.insertPlace(place)
    }

    suspend fun deletePlace(place: PlaceEntity) {
        return placeDao.deletePlace(place)
    }

    suspend fun getAllPlaces(): MutableList<PlaceEntity> {
        return placeDao.getAllPlaces()
    }
}