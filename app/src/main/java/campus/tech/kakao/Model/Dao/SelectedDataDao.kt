package campus.tech.kakao.Model.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import campus.tech.kakao.Model.Entity.SelectedData

@Dao
interface SelectedDataDao {
    @Insert
    suspend fun insert(selectedData: SelectedData): Long

    @Query("SELECT * FROM selected_data ORDER BY id DESC")
    suspend fun getAllSelectedData(): List<SelectedData>

    @Query("DELETE FROM selected_data WHERE id = :id")
    suspend fun deleteById(id: Long): Int
}