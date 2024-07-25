package campus.tech.kakao.map.repository

interface MapRepository {
    fun saveLastPosition(latitude: Double, longitude: Double)
    fun getLastPosition(): Pair<Double, Double>
}