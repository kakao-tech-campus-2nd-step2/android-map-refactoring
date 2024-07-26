package campus.tech.kakao.map.presentation

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.presentation.viewmodel.MapViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val TAG = "KAKAOMAP"

    private val viewModel : MapViewModel by viewModels()
    private lateinit var binding: ActivityMapBinding
    private lateinit var kakaoMap: KakaoMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)

        viewModel.lastLocation.observe(this) {
            it?.let { updateView(it) }
        }
        binding.kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // TODO: 필요시 구현 예정
            }

            override fun onMapError(exception: Exception?) {
                showErrorPage(exception)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                viewModel.updateLastLocation()
                Log.d(TAG, "onMapReady")
            }
        })

        binding.searchBox.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        binding.kakaoMapView.resume()
        viewModel.updateLastLocation()
    }

    override fun onPause() {
        super.onPause()
        binding.kakaoMapView.pause()
        Log.d("KAKAOMAP", "onPause")
    }

    private fun moveToTargetLocation(kakaoMap: KakaoMap, target: Location) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(target.y, target.x))
        kakaoMap.moveCamera(cameraUpdate)
        val labelManager = kakaoMap.labelManager
        labelManager?.let { setPin(labelManager, target) }
    }

    private fun showErrorPage(exception: Exception?) {
        binding.errorCode.text = exception?.message
        binding.errorLayout.isVisible = true
        binding.kakaoMapView.isVisible = false
        binding.searchBox.isVisible = false
    }

    private fun setPin(labelManager: LabelManager, target: Location) {
        labelManager.removeAllLabelLayer()
        val style = labelManager
            .addLabelStyles(
                LabelStyles.from(
                    LabelStyle.from(R.drawable.location_label).setTextStyles(30, Color.BLACK)
                )
            )
        labelManager.layer
            ?.addLabel(
                LabelOptions.from(LatLng.from(target.y, target.x))
                    .setStyles(style)
                    .setTexts(target.name)
            )
    }

    fun updateView(lastLocation: Location) {
        if (::kakaoMap.isInitialized) {
            binding.lastLoc = lastLocation
            moveToTargetLocation(kakaoMap, lastLocation)
            binding.infoSheet.isVisible = true
        }
    }
}