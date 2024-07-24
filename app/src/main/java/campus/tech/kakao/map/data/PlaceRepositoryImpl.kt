package campus.tech.kakao.map.data

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceRepositoryImpl(private val placeDB: PlaceDB):PlaceRepository{
    override suspend fun getPlaces(placeName: String): List<Place> {
        return placeDB.getPlaces(placeName)
    }

    override suspend fun updatePlaces(places: List<Place>) {
        placeDB.updatePlaces(places)
    }

    override suspend fun getPlaceById(id: String): Place? {
        return placeDB.getPlaceById(id)
    }

    override suspend fun getLogs(): List<Place> {
        return placeDB.getLogs()
    }

    override suspend fun updateLogs(placeLog: List<Place>) {
        placeDB.updateLogs(placeLog)
    }

    override suspend fun removeLog(id: String) {
        placeDB.removeLog(id)
    }

    override suspend fun saveLastVisitedPlace(place: Place) {
        placeDB.saveLastVisitedPlace(place)
    }

    override suspend fun getLastVisitedPlace(): Place? {
        return placeDB.getLastVisitedPlace()
    }
}