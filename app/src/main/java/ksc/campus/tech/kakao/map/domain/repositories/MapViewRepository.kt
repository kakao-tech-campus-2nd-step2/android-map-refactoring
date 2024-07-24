package ksc.campus.tech.kakao.map.domain.repositories

import com.kakao.vectormap.camera.CameraPosition
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.data.repositoryimpls.MapViewRepositoryImpl
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import javax.inject.Singleton

interface MapViewRepository {
    val selectedLocation: SharedFlow<LocationInfo?>
    val cameraPosition: SharedFlow<CameraPosition>

    suspend fun loadFromSharedPreference()
    suspend fun updateSelectedLocation(locationInfo: LocationInfo)
    suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double)
    suspend fun updateCameraPosition(position: CameraPosition)
    suspend fun clearSelectedLocation()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MapRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMapRepository(
        mapRepositoryImpl: MapViewRepositoryImpl
    ): MapViewRepository
}