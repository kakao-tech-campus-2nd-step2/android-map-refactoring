package campus.tech.kakao.map.view

import campus.tech.kakao.map.data.Place
import campus.tech.kakao.map.data.SavedPlace

interface OnClickPlaceListener {
    fun savePlace(place: Place)
}

interface OnClickSavedPlaceListener {
    fun deleteSavedPlace(savedPlace: SavedPlace)
    fun loadPlace(savedPlace: SavedPlace)
}