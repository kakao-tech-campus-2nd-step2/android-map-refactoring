package campus.tech.kakao.map.domain.repository

import com.kakao.vectormap.LatLng
import kotlinx.coroutines.flow.Flow

interface LastLocationRepository {
    suspend fun saveLocation(latitude: Double, longitude: Double)
    fun loadLocation(): Flow<LatLng?>
}