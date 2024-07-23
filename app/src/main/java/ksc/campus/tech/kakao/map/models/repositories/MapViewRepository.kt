package ksc.campus.tech.kakao.map.models.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.kakao.vectormap.camera.CameraPosition
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import javax.inject.Singleton

data class LocationInfo(
    val address: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

interface MapViewRepository {
    val selectedLocation: SharedFlow<LocationInfo?>
    val cameraPosition: SharedFlow<CameraPosition>

    suspend fun loadFromSharedPreference(context: Context)
    suspend fun updateSelectedLocation(context: Context, locationInfo: LocationInfo)
    suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double)
    suspend fun updateCameraPosition(context: Context, position: CameraPosition)
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