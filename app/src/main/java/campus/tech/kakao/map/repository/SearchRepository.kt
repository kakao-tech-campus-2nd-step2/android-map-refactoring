package campus.tech.kakao.map.repository

import campus.tech.kakao.map.model.PlaceInfo
import campus.tech.kakao.map.model.SavePlace

interface SearchRepository {
    suspend fun savePlaces(placeName: String): List<SavePlace>
    suspend fun showSavePlace(): List<SavePlace>
    suspend fun deleteSavedPlace(savedPlaceName: String): List<SavePlace>
    fun getPlaceList(categoryGroupName: String, callback: (List<PlaceInfo>?) -> Unit)
}