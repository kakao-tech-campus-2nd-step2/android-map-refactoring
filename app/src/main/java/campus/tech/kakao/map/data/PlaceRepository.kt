package campus.tech.kakao.map.data

import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDatabase: PlaceDatabase) {
    suspend fun insertPlace(place: PlaceEntity) {
        return placeDatabase.placeDao().insertPlace(place)
    }

    suspend fun deletePlace(place: PlaceEntity) {
        return placeDatabase.placeDao().deletePlace(place)
    }

    suspend fun getAllPlaces(): MutableList<PlaceEntity> {
        return placeDatabase.placeDao().getAllPlaces()
    }
}