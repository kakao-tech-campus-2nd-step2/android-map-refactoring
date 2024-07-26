package campus.tech.kakao.map.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ErrorLayoutBinding
import campus.tech.kakao.map.databinding.MapLayoutBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var mapBinding: MapLayoutBinding
    private lateinit var labelManager: LabelManager
    private val viewModel: MapViewModel by viewModels()

    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            labelManager = kakaoMap.labelManager!!
            viewModel.updateLocationFromIntent(intent)
            displayMapLocation(kakaoMap)
            viewModel.saveLocation()
            viewModel.logSavedLocation()

            viewModel.hasIntentData.observe(this@MapActivity, Observer { hasIntentData ->
                if (hasIntentData) {
                    displayMapLocation(kakaoMap)
                    displayMarker()
                    displayBottomSheet()
                }
            })
        }

        override fun getPosition(): LatLng {
            return viewModel.startPosition
        }

        override fun getZoomLevel(): Int {
            return viewModel.startZoomLevel
        }

    }

    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {

        override fun onMapDestroy() {
            Toast.makeText(applicationContext, "onMapDestroy", Toast.LENGTH_SHORT).show()
        }

        override fun onMapError(error: Exception) {
            viewModel.onMapError(error)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapBinding = DataBindingUtil.setContentView(this, R.layout.map_layout)
        mapBinding.mapView.start(lifeCycleCallback, readyCallback)
        mapBinding.etSearch.setOnClickListener {
            val intent = Intent(this, PlaceActivity::class.java)
            startActivity(intent)
        }

        viewModel.errorMessage.observe(this, Observer { message ->
            if (message != null) {
                val errorBinding: ErrorLayoutBinding = DataBindingUtil.setContentView(this, R.layout.error_layout)
                errorBinding.viewModel = viewModel
                errorBinding.lifecycleOwner = this
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mapBinding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapBinding.mapView.pause()
    }

    private fun initializeMap() {
        mapBinding.mapView.start(lifeCycleCallback, readyCallback)
    }

    private fun displayMapLocation(kakaoMap: KakaoMap) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(viewModel.longitude.value!!.toDouble(), viewModel.latitude.value!!.toDouble())
        )
        kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    private fun displayMarker() {
        val pos = LatLng.from(viewModel.longitude.value!!.toDouble(), viewModel.latitude.value!!.toDouble())
        val yellowMarker = labelManager.addLabelStyles(
            LabelStyles.from("yellowMarker", LabelStyle.from(R.drawable.yellow_marker))
        )
        labelManager.layer!!.addLabel(
            LabelOptions.from("label", pos)
                .setStyles(yellowMarker)
        )
    }

    private fun displayBottomSheet() {
        val dataBundle = Bundle().apply {
            putString("name", viewModel.name.value)
            putString("address", viewModel.address.value)
        }
        val modal = ModalBottomSheet()
        modal.arguments = dataBundle
        modal.show(supportFragmentManager, "modalBottomSheet")
    }

}