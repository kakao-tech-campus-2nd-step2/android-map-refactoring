package campus.tech.kakao.map

import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location

interface DatabaseListener {
    fun deleteHistory(oldHistory: History)
    fun insertHistory(newHistory: History)
    fun searchHistory(locName: String, isExactMatch: Boolean)
    fun showMap()
    fun insertLastLocation(location: Location)
}