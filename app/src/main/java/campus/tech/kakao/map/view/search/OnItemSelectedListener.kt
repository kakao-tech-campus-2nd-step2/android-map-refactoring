package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SavedLocation

interface OnItemSelectedListener {
    fun onLocationViewClicked(location: Location)
    fun onSavedLocationClearButtonClicked(item: SavedLocation)
    fun onSavedLocationViewClicked(title: String)
}