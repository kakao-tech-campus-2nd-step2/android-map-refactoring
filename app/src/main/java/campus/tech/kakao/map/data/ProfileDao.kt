package campus.tech.kakao.map.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles")
    fun getAll(): List<Profile>

    @Insert
    fun insertAll(vararg profiles: Profile)

}