package campus.tech.kakao.map.data

import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository

class PlaceRepositoryImpl(private val dbHelper: PlaceDBHelper):PlaceRepository{
    override suspend fun getPlaces(placeName: String): List<Place> {
        return dbHelper.getPlaces(placeName)
    }

    override suspend fun updatePlaces(places: List<Place>) {
        dbHelper.updatePlaces(places)
    }

    override fun getPlaceById(id: String): Place? {
        return dbHelper.getPlaceById(id)
    }

    override fun getLogs(): List<Place> {
        return dbHelper.getLogs()
    }

    override fun updateLogs(placeLog: List<Place>) {
        dbHelper.updateLogs(placeLog)
    }

    override fun removeLog(id: String) {
        dbHelper.removeLog(id)
    }
}