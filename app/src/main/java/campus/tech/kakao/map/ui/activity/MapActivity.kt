package campus.tech.kakao.map.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.LayoutMapBottomSheetDialogBinding
import campus.tech.kakao.map.ui.viewmodel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: LinearLayout
    private lateinit var bottomSheet: BottomSheetDialog
    private lateinit var binding: LayoutMapBottomSheetDialogBinding

    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchButton = findViewById(R.id.search_button)

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_map_bottom_sheet_dialog, null, false)
        binding.viewModel = mapViewModel
        binding.lifecycleOwner = this

        bottomSheet = BottomSheetDialog(this)
        mapViewModel.loadIntentData(intent)
        mapViewModel.showBottomSheet.observe(this, Observer { show ->
            if (show) {
                bottomSheet.setContentView(binding.root)
                bottomSheet.show()
            } else {
                bottomSheet.dismiss()
            }
        })
        initMap()
        initUI()
    }

    private fun initMap() {
        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {}

                override fun onMapError(error: Exception) {
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    mapViewModel.name.value?.let {
                        addMarker(kakaoMap, mapViewModel.latitude.value ?: "35.231627", mapViewModel.longitude.value ?: "129.084020", it)
                        mapViewModel.saveLatLng() // Save lat and lng to shared preferences
                        mapViewModel.setShowBottomSheet(true)
                    }
                }

                override fun getPosition(): LatLng {
                    val lat = mapViewModel.latitude.value?.toDouble() ?: 35.231627
                    val lng = mapViewModel.longitude.value?.toDouble() ?: 129.084020
                    return LatLng.from(lat, lng)
                }
            }
        )
    }

    private fun addMarker(kakaoMap: KakaoMap, latitude: String, longitude: String, name: String) {
        val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.location_marker).setTextStyles(25, Color.BLACK)))
        val options = LabelOptions.from(LatLng.from(latitude.toDouble(), longitude.toDouble())).setStyles(styles)
        val layer = kakaoMap.labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)
    }

    private fun initUI() {
        searchButton.setOnClickListener {
            val mapToSearchIntent = Intent(this@MapActivity, SearchActivity::class.java)
            startActivity(mapToSearchIntent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }
}
