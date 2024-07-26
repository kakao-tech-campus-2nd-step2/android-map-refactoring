package campus.tech.kakao.map.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.viewmodel.MapViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapViewBinding
import campus.tech.kakao.map.repository.MapRepositoryInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapViewBinding
    @Inject lateinit var mapRepository: MapRepositoryInterface
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupBottomSheetBehavior()

        val lastLocation = mapViewModel.getLastLocation()

        val placeName = intent.getStringExtra(MainActivity.EXTRA_PLACE_NAME)
        val placeAddr = intent.getStringExtra(MainActivity.EXTRA_PLACE_ADDR)
        val placeX = intent.getStringExtra(MainActivity.EXTRA_PLACE_X) ?: DEFAULT_LONGITUDE
        val placeY = intent.getStringExtra(MainActivity.EXTRA_PLACE_Y) ?: DEFAULT_LATITUDE

        observeBottomSheetStateChanges()
        mapViewModel.setPlaceInfo(placeName, placeAddr)

        try {
            binding.map.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    Log.d("KakaoMap", "카카오맵 정상종료")
                }

                override fun onMapError(exception: Exception?) {
                    val errorMsg = mapViewModel.extractErrorMsg(exception.toString())
                    val intent = Intent(this@MapViewActivity, ErrorActivity::class.java)
                    intent.putExtra(EXTRA_ERROR_MSG, errorMsg)
                    startActivity(intent)
                    Log.d("KakaoMap", errorMsg.toString())
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    Log.d("KakaoMap", "카카오맵 정상실행")
                    val position: LatLng
                    if (!placeName.isNullOrEmpty() && !placeAddr.isNullOrEmpty()) {
                        position = LatLng.from(placeY.toDouble(), placeX.toDouble())

                        val style = map.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(
                            R.drawable.flag
                        ).setTextStyles(40, Color.BLACK)))
                        val options: LabelOptions = LabelOptions.from(position)
                            .setStyles(style)
                        val layer = map.labelManager?.layer
                        val label = layer?.addLabel(options)
                        label?.changeText(placeName)
                    } else {
                        val (x, y) = lastLocation ?: Pair(DEFAULT_LONGITUDE.toDouble(), DEFAULT_LATITUDE.toDouble())
                        position = LatLng.from(y, x)
                    }
                    //카메라 이동
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
                    map.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
                }

            })
            Log.d("MapViewActivity", "mapView start called")
        } catch (e: Exception) {
            Log.e("MapViewActivity", "Exception during mapView.start", e)
        }
    }

    fun onSearchTextViewClick() {
        startActivity(Intent(this@MapViewActivity, MainActivity::class.java))
    }
    private fun setupBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_view)
        binding.view = this
        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this
    }
    private fun setupBottomSheetBehavior(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomView.bottomSheetLayout)
    }
    private fun observeBottomSheetStateChanges(){
        mapViewModel.bottomSheetState.observe(this) { state ->
            bottomSheetBehavior.state = state
        }
    }
    override fun onResume() {
        super.onResume()
        binding.map.resume()
    }
    override fun onPause() {
        super.onPause()
        binding.map.pause()
    }
    companion object {
        const val EXTRA_ERROR_MSG = "ERROR"
        const val DEFAULT_LONGITUDE = "127.0016985"
        const val DEFAULT_LATITUDE = "37.5642135"
    }
}
