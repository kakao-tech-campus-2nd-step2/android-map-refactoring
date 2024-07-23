package campus.tech.kakao.map.Data.Datasource.Local.Dao

import androidx.room.Dao
import androidx.room.Query
import campus.tech.kakao.map.Data.Datasource.Local.Entity.PlaceEntity
import campus.tech.kakao.map.Data.Datasource.Local.PlaceContract

@Dao
interface PlaceDao {
    @Query("DELETE FROM ${PlaceContract.PlaceEntry.TABLE_NAME} WHERE ${PlaceContract.PlaceEntry.COLUMN_ID} = :id")
    fun deletePlace(id : Int)
    @Query("SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME} WHERE ${PlaceContract.PlaceEntry.COLUMN_NAME} LIKE :name")
    fun getSimilarPlacesByName(name: String) : List<PlaceEntity>
    @Query("SELECT * FROM ${PlaceContract.PlaceEntry.TABLE_NAME} WHERE ${PlaceContract.PlaceEntry.COLUMN_ID} = :id")
    fun getPlaceById(id : Int): PlaceEntity?
}