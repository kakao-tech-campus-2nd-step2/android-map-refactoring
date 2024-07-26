package campus.tech.kakao.map.repository.map.datasource

interface MapDataSource {
    fun updateLastPosition(latitude: Double, longitude: Double)
    fun readLastPosition(): Pair<Double?, Double?>
}
