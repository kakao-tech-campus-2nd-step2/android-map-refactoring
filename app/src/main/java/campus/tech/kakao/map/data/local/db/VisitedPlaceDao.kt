package campus.tech.kakao.map.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity

@Dao
interface VisitedPlaceDao {
    @Query("SELECT * FROM visited_places ORDER BY id DESC LIMIT 1")
    fun getLastPlace(): VisitedPlaceEntity?

    @Insert
    fun insert(place: VisitedPlaceEntity)
}