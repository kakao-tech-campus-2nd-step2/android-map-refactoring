package campus.tech.kakao.map.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaceDao {
    @Query("SELECT * FROM place WHERE placeName LIKE :query OR placeAddress LIKE :query")
    suspend fun search(query: String): List<PlaceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: PlaceEntity)

    @Query("SELECT * FROM place WHERE saved = 1")
    suspend fun getSavedSearches(): List<PlaceEntity>

    @Query("UPDATE place SET saved = :saved WHERE id = :id")
    suspend fun updateSavedStatus(id: Int, saved: Boolean)
}