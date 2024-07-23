package ksc.campus.tech.kakao.map.models.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ksc.campus.tech.kakao.map.models.entities.SearchKeyword

@Dao
interface SearchKeywordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: SearchKeyword)

    @Delete
    fun delete(entity: SearchKeyword)

    @Query("SELECT * FROM ${SearchKeyword.TABLE_NAME}")
    fun queryAllKeywords(): List<SearchKeyword>

    @Query("DELETE FROM ${SearchKeyword.TABLE_NAME} WHERE ${SearchKeyword.COLUMN_KEYWORD} = :name")
    fun deleteWhere(name: String)
}