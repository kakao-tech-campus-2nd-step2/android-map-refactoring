package campus.tech.kakao.map.dto

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchWordDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(word: SearchWord)

	@Query("DELETE FROM SearchWord WHERE name = :name AND address = :address AND type = :type")
	suspend fun delete(name:String, address:String, type:String)

	@Query("SELECT * FROM SearchWord")
	suspend fun getAll(): List<SearchWord>

	@Query("SELECT * FROM SearchWord WHERE name = :name AND address = :address AND type = :type")
	suspend fun get(name:String, address:String, type:String): SearchWord

}