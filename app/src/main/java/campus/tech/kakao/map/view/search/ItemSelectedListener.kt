package campus.tech.kakao.map.view.search

import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.SavedLocation

interface ItemSelectedListener {
    fun onLocationItemClicked(location: Location)
    fun onSavedLocationItemClearButtonClicked(item: SavedLocation)
    fun onSavedLocationItemClicked(title: String)
}