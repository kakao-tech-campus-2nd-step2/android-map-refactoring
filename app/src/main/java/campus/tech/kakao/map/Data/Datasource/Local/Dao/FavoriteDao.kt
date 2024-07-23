package campus.tech.kakao.map.Data.Datasource.Local.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.Data.Datasource.Local.Entity.FavoriteEntity
import campus.tech.kakao.map.Data.Datasource.Local.PlaceContract

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM ${PlaceContract.FavoriteEntry.TABLE_NAME}")
    fun getCurrentFavorite(): MutableList<FavoriteEntity>
    @Query("DELETE FROM ${PlaceContract.FavoriteEntry.TABLE_NAME} WHERE ${PlaceContract.FavoriteEntry.COLUMN_ID} = :id")
    fun deleteFavorite(id : Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavorite(place: FavoriteEntity)

    @Query("SELECT * FROM ${PlaceContract.FavoriteEntry.TABLE_NAME} WHERE ${PlaceContract.FavoriteEntry.COLUMN_ID} = :id")
    fun getFavoriteById(id: Int): FavoriteEntity?
}