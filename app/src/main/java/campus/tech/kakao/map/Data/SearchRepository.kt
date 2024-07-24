package campus.tech.kakao.map.Data

class SearchRepository(private val searchDao: SearchDao) {
    suspend fun insertSearchResult(text: String) {
        searchDao.insertSearchResult(SearchResult(text = text))
    }

    suspend fun deleteSearchResult(searchResult: SearchResult) {
        searchDao.deleteSearchResult(searchResult)
    }

    suspend fun getSearchResults(text: String): List<SearchResult> {
        return searchDao.getSearchResults("%$text%")
    }
}
