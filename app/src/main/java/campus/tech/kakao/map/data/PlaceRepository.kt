package campus.tech.kakao.map.data

import javax.inject.Inject

class PlaceRepository @Inject constructor(private val placeDatabase: PlaceDatabase) {
    suspend fun insertPlace(place: Place) {
        return placeDatabase.placeDao().insertPlace(place)
    }

    suspend fun deletePlace(place: Place) {
        return placeDatabase.placeDao().deletePlace(place)
    }

    suspend fun getAllPlaces(): MutableList<Place> {
        return placeDatabase.placeDao().getAllPlaces()
    }
}