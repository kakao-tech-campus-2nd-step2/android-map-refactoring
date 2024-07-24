package ksc.campus.tech.kakao.map.models.datasources

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.kakao.vectormap.camera.CameraPosition
import ksc.campus.tech.kakao.map.models.mappers.cameraPositionMapper.CameraPositionDeserializer
import ksc.campus.tech.kakao.map.models.mappers.cameraPositionMapper.CameraPositionSerializer
import ksc.campus.tech.kakao.map.models.repositories.LocationInfo
import javax.inject.Inject

interface OnMapPreferenceChanged {
    fun onCameraPositionChanged()

    fun onSelectedLocationChanged()
}

class MapPreferenceLocalDataSource @Inject constructor() {
    private val cameraPositionSerializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionSerializer::class.java, CameraPositionSerializer())
        .create()
    private val cameraPositionDeserializer: Gson = GsonBuilder()
        .registerTypeAdapter(CameraPositionDeserializer::class.java, CameraPositionDeserializer())
        .create()
    private val gson: Gson = Gson()
    private val sharedPreferenceChangeListener = OnMapSharedPreferenceChanged()
    private var sharedPreference:SharedPreferences? = null

    private fun getSharedPreference(context: Context):SharedPreferences{
        if(sharedPreference == null){
            sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        }
        return sharedPreference!!
    }

    class OnMapSharedPreferenceChanged: OnSharedPreferenceChangeListener{
        var onPreferenceChanged:OnMapPreferenceChanged? = null
        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            Log.d("KSC", "Shared Preference Changed")
            if (key.equals(CAMERA_POSITION_KEY)) {
                onPreferenceChanged?.onCameraPositionChanged()
            }
            if (key.equals(SELECTED_LOCATION_KEY)) {
                onPreferenceChanged?.onSelectedLocationChanged()
            }
        }
    }

    fun getCameraPosition(context: Context): CameraPosition? {
        val data = getSharedPreference(context).getString(CAMERA_POSITION_KEY, "")

        if (data.isNullOrEmpty()) {
            return null
        }

        try {
            return cameraPositionDeserializer.fromJson(data, CameraPosition::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun getSelectedLocation(context: Context): LocationInfo? {
        val data = getSharedPreference(context).getString(SELECTED_LOCATION_KEY, "")

        if (data.isNullOrEmpty()) {
            return null
        }

        try {
            return gson.fromJson(data, LocationInfo::class.java)
        } catch (e: JsonSyntaxException) {
            Log.e("KSC", e.message ?: "")
            return null
        }
    }

    fun saveCameraPosition(context: Context, position: CameraPosition) {
        val editor = getSharedPreference(context).edit()
        editor.putString(CAMERA_POSITION_KEY, cameraPositionSerializer.toJson(position))
        editor.apply()
        Log.d("KSC", "scp")
    }

    fun saveSelectedLocation(context: Context, location: LocationInfo) {
        val editor = getSharedPreference(context).edit()
        editor.putString(SELECTED_LOCATION_KEY, gson.toJson(location))
        editor.apply()
        Log.d("KSC", "ssl")
    }

    fun setOnPreferenceChanged(context: Context, onChanged: OnMapPreferenceChanged){
        sharedPreferenceChangeListener.onPreferenceChanged = onChanged
        getSharedPreference(context).registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener)
        Log.d("KSC", "registering setOnPreferenceChanged")
    }

    companion object {
        private const val PREFERENCE_NAME = "mapViewPreference"
        private const val CAMERA_POSITION_KEY = "currentPosition"
        private const val SELECTED_LOCATION_KEY = "selectedLocation"
    }
}