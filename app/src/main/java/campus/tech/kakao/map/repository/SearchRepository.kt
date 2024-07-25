package campus.tech.kakao.map.repository

import android.content.Context
import androidx.room.Room
import campus.tech.kakao.map.model.AppDatabase
import campus.tech.kakao.map.model.SavePlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "placedb"
    ).build()
    private val savePlaceDao = db.savePlaceDao()

    suspend fun savePlaces(placeName: String) {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(placeName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
            savePlaceDao.insert(SavePlace(savePlaceName = placeName))
        }
    }

    suspend fun showSavePlace(): List<SavePlace> {
        return withContext(Dispatchers.IO) {
            savePlaceDao.getAll()
        }
    }

    suspend fun deleteSavedPlace(savedPlaceName: String) {
        withContext(Dispatchers.IO) {
            val existingPlace = savePlaceDao.getByName(savedPlaceName)
            if (existingPlace != null) {
                savePlaceDao.delete(existingPlace)
            }
        }
    }

    suspend fun savePlacesAndUpdate(placeName: String): List<SavePlace> {
        savePlaces(placeName)
        return showSavePlace()
    }

    suspend fun deleteSavedPlacesAndUpdate(savedPlaceName: String): List<SavePlace> {
        deleteSavedPlace(savedPlaceName)
        return showSavePlace()
    }
}
