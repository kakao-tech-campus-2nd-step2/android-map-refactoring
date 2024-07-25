package campus.tech.kakao.map.model.repository

import campus.tech.kakao.map.model.SavedLocation

interface SavedLocationRepository {
    suspend fun getSavedLocationAll(): MutableList<SavedLocation>
    suspend fun addSavedLocation(savedLocation: SavedLocation)
    suspend fun deleteSavedLocation(savedLocation: SavedLocation)
}