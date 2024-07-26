package campus.tech.kakao.map.repository.local

import campus.tech.kakao.map.model.local.Repository
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao
) : Repository {
    override suspend fun getPlace(): List<PlaceEntity> {
        return placeDao.getSavedSearches().map { entity ->
            PlaceEntity(entity.id, entity.placeName, entity.placeAddress, entity.saved)
        }
    }

    override suspend fun addPlace(locationName: String) {
        val newPlace = PlaceEntity(placeName = locationName, placeAddress = "Unknown")
        placeDao.insert(newPlace)
    }

    override suspend fun removePlace(locationName: String) {
        val place = placeDao.search("%$locationName%")
        place.forEach { placeDao.updateSavedStatus(it.id, false) }
    }
}