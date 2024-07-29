package campus.tech.kakao.map.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
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

@AndroidEntryPoint
class HomeMapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeMapBinding

    private lateinit var placeNameTextView: TextView
    private lateinit var placeAddressTextView: TextView

    private val mapViewModel: MapViewModel by viewModels()

    private val bottomSheet: ConstraintLayout by lazy { findViewById(R.id.bottomSheet) }
    private lateinit var bottomBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setBinding()

        val name = intent.getStringExtra(LocationDataContract.LOCATION_NAME).toString()
        val address = intent.getStringExtra(LocationDataContract.LOCATION_ADDRESS).toString()
        val latitude = intent.getStringExtra(LocationDataContract.LOCATION_LATITUDE)?.toDouble()
        val longitude = intent.getStringExtra(LocationDataContract.LOCATION_LONGITUDE)?.toDouble()

        placeNameTextView = findViewById(R.id.placeName)
        placeAddressTextView = findViewById(R.id.placeAddress)

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
                if (latitude != null && longitude != null) {
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
                        LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(style)
                            .setTexts(name)
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

                return if (latitude != null && longitude != null) {
                    LatLng.from(latitude, longitude)
                } else if (savedLatitude != null && savedLongitude != null) {
                    LatLng.from(savedLatitude, savedLongitude)
                } else {
                    super.getPosition()
                }
            }
        })

        if (latitude != null && longitude != null) {
            placeNameTextView.text = name
            placeAddressTextView.text = address
            bottomBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
    }
}