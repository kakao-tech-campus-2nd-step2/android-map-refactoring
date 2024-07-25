package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.Keyword

interface Repository {
    suspend fun search(query: String): List<Keyword>
    suspend fun getAllSavedKeywordsFromPrefs(): List<Keyword>
    suspend fun saveKeywordToPrefs(keyword: Keyword)
    suspend fun deleteKeywordFromPrefs(keyword: Keyword)
    suspend fun saveLastMarkerPosition(latitude: Double, longitude: Double, placeName: String, roadAddressName: String)
    suspend fun loadLastMarkerPosition(): Keyword?
}
