package campus.tech.kakao.map.repository.location

import androidx.room.Dao
import androidx.room.Query
import campus.tech.kakao.map.entity.LocationEntity

@Dao
interface LocationDao { // ItemDao 이름 변경
    @Query("SELECT * FROM location WHERE category_group_name = :category")
    suspend fun searchByCategory(category: String): List<LocationEntity>
}
