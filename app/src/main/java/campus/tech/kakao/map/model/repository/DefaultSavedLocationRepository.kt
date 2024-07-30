package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.datasource.SavedLocationDao
import campus.tech.kakao.map.model.datasource.SavedLocationDatabase
import javax.inject.Inject

class DefaultSavedLocationRepository @Inject constructor(
    private val savedLocationDao: SavedLocationDao
): SavedLocationRepository {
    override suspend fun getSavedLocationAll(): MutableList<SavedLocation> {
        val results = savedLocationDao.getAll()
        return if(results.isNotEmpty()) results.toMutableList() else mutableListOf()
    }

    override suspend fun addSavedLocation(savedLocation: SavedLocation) {
        savedLocationDao.insert(savedLocation)
    }

    override suspend fun deleteSavedLocation(savedLocation: SavedLocation) {
        savedLocationDao.delete(savedLocation)
    }

}