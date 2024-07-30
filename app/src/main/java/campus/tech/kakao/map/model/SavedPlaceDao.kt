package campus.tech.kakao.map.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.db.PlaceContract

@Dao
interface SavedPlaceDao {

    @Query("SELECT * FROM ${PlaceContract.SavedPlaceEntry.TABLE_NAME}")
    fun readSavedPlaceData(): List<SavedPlace>

    @Query("SELECT * FROM ${PlaceContract.SavedPlaceEntry.TABLE_NAME} WHERE ${PlaceContract.SavedPlaceEntry.COLUMN_NAME} = :name")
    fun readSavedPlaceDataWithSamedName(name: String): List<SavedPlace>

    @Query("DELETE FROM ${PlaceContract.SavedPlaceEntry.TABLE_NAME} WHERE ${PlaceContract.SavedPlaceEntry.COLUMN_NAME} = :name")
    fun deleteSavedPlace(name: String)

    @Query("INSERT INTO ${PlaceContract.SavedPlaceEntry.TABLE_NAME} (${PlaceContract.SavedPlaceEntry.COLUMN_NAME}) VALUES (:name)")
    fun insertSavedPlaceData(name:String)
}