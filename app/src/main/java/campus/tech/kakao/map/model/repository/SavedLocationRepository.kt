package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.datasource.AppDatabase
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationRepository(
    private val appDatabase: AppDatabase
) {
    suspend fun getSavedLocationAll(): MutableList<SavedLocation> {
        val results = appDatabase.savedLocationDao().getAll()
        return if(results.isNotEmpty()) results.toMutableList() else mutableListOf()
    }

    suspend fun addSavedLocation(savedLocation: SavedLocation) {
        appDatabase.savedLocationDao().insert(savedLocation)
    }

    suspend fun deleteSavedLocation(savedLocation: SavedLocation) {
        appDatabase.savedLocationDao().delete(savedLocation)
    }

}