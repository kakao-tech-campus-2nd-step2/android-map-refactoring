package campus.tech.kakao.map.view

import campus.tech.kakao.map.model.Location

interface OnSearchItemClickListener {
    fun onSearchItemClick(location: Location)
}