package campus.tech.kakao.map.repository

import campus.tech.kakao.map.data.Keyword

interface Repository {
    suspend fun search(query: String): List<Keyword>
    fun getAllSavedKeywordsFromPrefs(): List<Keyword>
    fun saveKeywordToPrefs(keyword: Keyword)
    fun deleteKeywordFromPrefs(keyword: Keyword)
    fun saveLastMarkerPosition(latitude: Double, longitude: Double, placeName: String, roadAddressName: String)
    fun loadLastMarkerPosition(): Keyword?
}
