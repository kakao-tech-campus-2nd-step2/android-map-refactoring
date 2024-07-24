package ksc.campus.tech.kakao.map.repositoriesImpl

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.camera.CameraPosition
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ksc.campus.tech.kakao.map.models.repositories.LocationInfo
import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.models.repositoriesImpl.MapViewRepositoryImpl
import javax.inject.Inject

class FakeMapViewRepository @Inject constructor(): MapViewRepository {

    private var _selectedLocation = MutableSharedFlow<LocationInfo?>(
        replay = 1,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private var _cameraPosition = MutableSharedFlow<CameraPosition>(
        replay = 1,
        extraBufferCapacity = 3,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )


    override suspend fun loadFromSharedPreference() {
        updateCameraPosition(initialCameraPosition)
    }
    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(
        latitude,
        longitude,
        ZOOMED_CAMERA_ZOOM_LEVEL,
        ZOOMED_CAMERA_TILT_ANGLE,
        ZOOMED_CAMERA_ROTATION_ANGLE,
        ZOOMED_CAMERA_HEIGHT
    )

    override suspend fun updateSelectedLocation(locationInfo: LocationInfo) {
        _selectedLocation.emit(locationInfo)
    }

    override suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double) {
        _cameraPosition.emit(getZoomCameraPosition(latitude, longitude))
    }

    override suspend fun updateCameraPosition(position: CameraPosition) {
        _cameraPosition.emit(position)
    }

    override suspend fun clearSelectedLocation() {
        _selectedLocation.emit(null)
    }

    companion object {
        private const val ZOOMED_CAMERA_ZOOM_LEVEL = 18
        private const val ZOOMED_CAMERA_TILT_ANGLE = 0.0
        private const val ZOOMED_CAMERA_ROTATION_ANGLE = 0.0
        private const val ZOOMED_CAMERA_HEIGHT = -1.0

        val initialCameraPosition: CameraPosition = CameraPosition.from(
            35.8905341232321,
            128.61213266480294,
            15,
            0.0,
            0.0,
            -1.0
        )
    }

    override val selectedLocation: SharedFlow<LocationInfo?>
        get() = _selectedLocation
    override val cameraPosition: SharedFlow<CameraPosition>
        get() = _cameraPosition
}