package campus.tech.kakao.map.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding
    private val viewModel: MapViewModel by viewModels()

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_map)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.mapViewModel = viewModel
        binding.lifecycleOwner = this

        sharedPreferences = getSharedPreferences("map_prefs", Context.MODE_PRIVATE)


        binding.refreshImageView.setOnClickListener {
            retryMapLoad()
        }

        binding.inputSearchMap.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        startMapView()

    }

    private fun startMapView() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                //지도 API가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception?) {
                //지도 사용 중 에러가 발생할 때 호출됨
                error?.let { showErrorLayout(it) }
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                //인증 후 API가 정상적으로 실행될 때 호출됨

                val lastLat = sharedPreferences.getFloat("last_lat", 0.0f)
                val lastLng = sharedPreferences.getFloat("last_lng", 0.0f)
                val lastZoom = sharedPreferences.getFloat("last_zoom", 15.0f)

                if (lastLat != 0.0f && lastLng != 0.0f) {
                    val lastPosition = LatLng.from(lastLat.toDouble(), lastLng.toDouble())
                    kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(lastPosition, lastZoom.toInt()))
                }

                handleIntent(kakaoMap)
                //showErrorLayout(Exception("강제 에러 호출"))

                kakaoMap.setOnCameraMoveEndListener { _, cameraPosition, _ ->
                    val currentPosition = cameraPosition.position
                    val currentZoom = cameraPosition.zoomLevel

                    sharedPreferences.edit().apply {
                        putFloat("last_lat", currentPosition.latitude.toFloat())
                        putFloat("last_lng", currentPosition.longitude.toFloat())
                        putFloat("last_zoom", currentZoom.toFloat())
                        apply()
                    }
                }
            }
        })
    }

    private fun retryMapLoad() {
        viewModel.setErrorVisible(false)
        binding.mapView.visibility = View.VISIBLE
        startMapView()
    }

    private fun showErrorLayout(error: Exception) {
        binding.mapView.visibility = View.GONE
        viewModel.setErrorVisible(true)
        viewModel.setErrorMessage(error.toString())
    }

    private fun handleIntent(kakaoMap: KakaoMap) {
        intent?.let {
            val placeName = it.getStringExtra("place_name") ?: return
            val placeAddress = it.getStringExtra("place_address") ?: return
            val placeX = it.getStringExtra("place_x") ?: return
            val placeY = it.getStringExtra("place_y") ?: return

            viewModel.setPlaceName(placeName)
            viewModel.setPlaceAddress(placeAddress)
            viewModel.setBottomSheetVisible(true)

            val markerBitmap = BitmapFactory.decodeResource(resources, R.drawable.marker)
            val scaledBitmap = Bitmap.createScaledBitmap(markerBitmap, 50, 50, true)

            val labelStyle = LabelStyle.from(scaledBitmap)
            val labelStyles = LabelStyles.from(labelStyle)
            val labelOptions = LabelOptions.from(LatLng.from(placeY.toDouble(), placeX.toDouble())).setStyles(labelStyles)
            val labelLayer = kakaoMap.labelManager?.layer
            labelLayer?.addLabel(labelOptions)
            kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(placeY.toDouble(), placeX.toDouble()), 15))

        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
