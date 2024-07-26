package campus.tech.kakao.map.repository

import android.util.Log
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.model.SavedPlace
import campus.tech.kakao.map.model.SavedPlaceDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedPlaceRepository @Inject constructor(private val savedPlaceDao : SavedPlaceDao){
    suspend fun getAllSavedPlace() : List<SavedPlace> = savedPlaceDao.readSavedPlaceData()


    suspend fun writePlace(place: Place){
        val savedList = savedPlaceDao.readSavedPlaceDataWithSamedName(place.name)
        if (savedList.isNotEmpty()) {
            Log.d("testt", "데이터 중복")
            // 입력의 시간순대로 정렬되기 떄문에 레코드 삭제후 다시 집어넣기
            savedPlaceDao.deleteSavedPlace(place.name)
            savedPlaceDao.insertSavedPlaceData(place.name)
        } else {
            savedPlaceDao.insertSavedPlaceData(place.name)
        }
    }

    suspend fun deleteSavedPlace(savedPlace: SavedPlace){
        savedPlaceDao.deleteSavedPlace(savedPlace.name)
    }
}