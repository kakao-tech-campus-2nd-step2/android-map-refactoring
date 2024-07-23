package ksc.campus.tech.kakao.map.models.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import com.kakao.vectormap.camera.CameraPosition
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import javax.inject.Singleton

data class LocationInfo(
    val address: String,
    val name: String,
    val latitude: Double,
    val longitude: Double
)

interface MapViewRepository {
    val selectedLocation: LiveData<LocationInfo?>
    val cameraPosition: LiveData<CameraPosition>

    fun loadFromSharedPreference(context: Context)
    fun updateSelectedLocation(context: Context, locationInfo: LocationInfo)
    fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double)
    fun updateCameraPosition(context: Context, position: CameraPosition)
    fun clearSelectedLocation()
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