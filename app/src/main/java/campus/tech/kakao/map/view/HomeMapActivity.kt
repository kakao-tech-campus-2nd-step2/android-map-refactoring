package campus.tech.kakao.map.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.LocationDataContract
import campus.tech.kakao.map.databinding.ActivityHomeMapBinding
import campus.tech.kakao.map.viewModel.MapViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.databinding.MapDetailBottomSheetBinding

@AndroidEntryPoint
class HomeMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeMapBinding
    private lateinit var bottomSheetBinding: MapDetailBottomSheetBinding

    private val mapViewModel: MapViewModel by viewModels()

    private val bottomSheet: ConstraintLayout by lazy { findViewById(R.id.bottomSheet) }
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setBinding()
        setViewModel()

        val placeName = intent.getStringExtra(LocationDataContract.LOCATION_NAME).toString()
        val placeAddress = intent.getStringExtra(LocationDataContract.LOCATION_ADDRESS).toString()
        val placeLatitude = intent.getStringExtra(LocationDataContract.LOCATION_LATITUDE)?.toDouble()
        val placeLongitude = intent.getStringExtra(LocationDataContract.LOCATION_LONGITUDE)?.toDouble()

        bottomBehavior = BottomSheetBehavior.from(bottomSheet)

        //KaKao Map UI에 띄우기
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(p0: Exception?) {
                setErrorIntent(p0)
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {

                // 라벨 생성
                if (placeLatitude != null && placeLongitude != null) {
                    p0.labelManager
                    val style =
                        p0.labelManager?.addLabelStyles(
                            LabelStyles.from(
                                LabelStyle.from(R.drawable.map_label).setTextStyles(
                                    35,
                                    Color.BLACK, 1, Color.RED
                                )
                            )
                        )
                    val options =
                        LabelOptions.from(LatLng.from(placeLatitude, placeLongitude)).setStyles(style)
                            .setTexts(placeName)
                    val layer = p0.labelManager?.layer
                    layer?.addLabel(options)
                }
            }

            // 지도 시작 시 위치 좌표 설정
            override fun getPosition(): LatLng {
                val savedLatitude =
                    mapViewModel.getLocation(LocationDataContract.LOCATION_LATITUDE, null)
                        .toDoubleOrNull()
                val savedLongitude =
                    mapViewModel.getLocation(LocationDataContract.LOCATION_LONGITUDE, null)
                        .toDoubleOrNull()

                return if (placeLatitude != null && placeLongitude != null) {
                    LatLng.from(placeLatitude, placeLongitude)
                } else if (savedLatitude != null && savedLongitude != null) {
                    LatLng.from(savedLatitude, savedLongitude)
                } else {
                    super.getPosition()
                }
            }
        })

        if (placeLatitude != null && placeLongitude != null) {
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            mapViewModel.updateInfo(placeName,placeAddress)
        } else {
            bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.searchbarHome.setOnClickListener {
            val intent = Intent(this, DataSearchActivity::class.java)
            startActivity(intent)
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

    override fun onDestroy() {
        super.onDestroy()
        intent.getStringExtra(LocationDataContract.LOCATION_LATITUDE)
            ?.let { mapViewModel.saveLocation(LocationDataContract.LOCATION_LATITUDE, it) }
        intent.getStringExtra(LocationDataContract.LOCATION_LONGITUDE)
            ?.let { mapViewModel.saveLocation(LocationDataContract.LOCATION_LONGITUDE, it) }
    }

    private fun setErrorIntent(errorMsg: Exception?) {
        val intentError = Intent(this@HomeMapActivity, MapErrorActivity::class.java)
        intentError.putExtra("ERROR_MESSAGE", errorMsg.toString())
        startActivity(intentError)
    }

    private fun setBinding(){
        binding = ActivityHomeMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        bottomSheetBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.map_detail_bottom_sheet,
            findViewById(R.id.bottomSheet),
            true
        )
        bottomSheetBinding.lifecycleOwner = this
    }

    private fun setViewModel(){
        bottomSheetBinding.viewModel = mapViewModel
    }
}