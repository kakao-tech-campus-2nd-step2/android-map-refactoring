package campus.tech.kakao.map.adapter.listener

interface SearchAdapterListener {
    fun displaySearchLocation(name: String, address: String, latitude: String, longitude: String)
}