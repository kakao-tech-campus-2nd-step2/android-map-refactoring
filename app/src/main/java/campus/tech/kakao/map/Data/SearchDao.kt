package campus.tech.kakao.map.Data

import androidx.room.*

@Dao
interface SearchDao {
    @Insert
    suspend fun insertSearchResult(searchResult: SearchResult)

    @Delete
    suspend fun deleteSearchResult(searchResult: SearchResult)

    @Query("SELECT * FROM search_results WHERE text LIKE :searchText")
    suspend fun getSearchResults(searchText: String): List<SearchResult>
}
