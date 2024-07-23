package ksc.campus.tech.kakao.map.models.repositoriesImpl

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.vectormap.camera.CameraPosition
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import ksc.campus.tech.kakao.map.models.datasources.MapPreferenceLocalDataSource
import ksc.campus.tech.kakao.map.models.repositories.LocationInfo
import ksc.campus.tech.kakao.map.models.repositories.MapViewRepository
import javax.inject.Inject


class MapViewRepositoryImpl @Inject constructor(
    private val mapPreferenceDataSource: MapPreferenceLocalDataSource,
    @ApplicationContext private val context:Context
) : MapViewRepository {

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

    override val selectedLocation: SharedFlow<LocationInfo?>
        get() = _selectedLocation

    override val cameraPosition: SharedFlow<CameraPosition>
        get() = _cameraPosition


    private fun getZoomCameraPosition(latitude: Double, longitude: Double) = CameraPosition.from(
        latitude,
        longitude,
        ZOOMED_CAMERA_ZOOM_LEVEL,
        ZOOMED_CAMERA_TILT_ANGLE, ZOOMED_CAMERA_ROTATION_ANGLE,
        ZOOMED_CAMERA_HEIGHT)

    private fun saveCurrentPositionToSharedPreference(context:Context, position: CameraPosition){
        mapPreferenceDataSource.saveCameraPosition(context, position)
    }

    private fun saveSelectedLocation(context:Context, location: LocationInfo){
        mapPreferenceDataSource.saveSelectedLocation(context, location)
    }

    private fun loadSavedCurrentPosition(context:Context): CameraPosition {
        val data = mapPreferenceDataSource.getCameraPosition(context)
        return data?: initialCameraPosition
    }

    private fun loadSavedSelectedLocation(context:Context): LocationInfo? {
        return mapPreferenceDataSource.getSelectedLocation(context)
    }

    override suspend fun loadFromSharedPreference(context:Context){
        val cameraPosition = loadSavedCurrentPosition(context)
        val selectedLocation = loadSavedSelectedLocation(context)

        updateCameraPosition(context, cameraPosition)
        if(selectedLocation != null)
            updateSelectedLocation(context, selectedLocation)
    }

    override suspend fun updateSelectedLocation(context:Context, locationInfo: LocationInfo){
        saveSelectedLocation(context, locationInfo)
        _selectedLocation.emit(locationInfo)
    }

    override suspend fun updateCameraPositionWithFixedZoom(latitude: Double, longitude: Double){
        updateCameraPosition(context, getZoomCameraPosition(latitude, longitude))
        Log.d("KSC", "(fixed zoom) updated position to ${latitude},${longitude}")
    }

    override suspend fun updateCameraPosition(context:Context, position: CameraPosition){
        saveCurrentPositionToSharedPreference(context, position)
        Log.d("KSC", "updated position to ${position.position.latitude},${position.position.longitude}")
        _cameraPosition.emit(position)
    }

    override suspend fun clearSelectedLocation(){
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
}