package campus.tech.kakao.map.repository.map

interface MapRepository {
    fun updateLastPosition(latitude: Double, longitude: Double)
    fun readLastPosition(): Pair<Double?, Double?>
}
