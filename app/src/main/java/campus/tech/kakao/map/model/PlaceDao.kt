package campus.tech.kakao.map.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import campus.tech.kakao.map.model.Place

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place)

    @Query("SELECT * FROM places WHERE name LIKE :query OR address LIKE :query OR category LIKE :query")
    fun searchDatabase(query: String): List<Place>

    @Query("SELECT COUNT(*) FROM places")
    fun isDBEmpty(): Int

    @Query("DELETE FROM places WHERE name = :name AND address = :address AND category = :category")
    fun deletePlace(name: String, address: String, category: String)

    @Query("DELETE FROM places WHERE name = :name")
    fun deleteName(name: String)
}