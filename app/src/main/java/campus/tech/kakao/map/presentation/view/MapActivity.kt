package campus.tech.kakao.map.presentation.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.presentation.viewmodel.MapViewModel
import campus.tech.kakao.map.utils.ApiKeyProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapView: MapView
    private lateinit var searchBox: CardView
    private lateinit var kakaoMap: KakaoMap
    private val mapViewModel: MapViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        KakaoMapSdk.init(this, ApiKeyProvider.KAKAO_API_KEY)
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindingViews()
        binding.lifecycleOwner = this

        handleIntentData()

        setUpMap()
        setUpSearchBox()
    }

    private fun bindingViews() {
        mapView = binding.mapView
        searchBox = binding.searchBox
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
    }

    private fun setUpSearchBox() {
        searchBox.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java))
        }
    }


    private fun setUpMap() {
        if (mapViewModel.lastPlace.value != null) {
            Log.d("testt", "last place: ${mapViewModel.lastPlace.value}")
            startMap(mapViewModel.lastPlace.value as PlaceVO)
        } else {
            Log.d("testt", "디폴트 맵")
            startDefaultMap()
        }
    }

    private fun handleIntentData() {
        val placeFromIntent = intent.getSerializableExtra("place")
        if (placeFromIntent != null) {
            mapViewModel.setLastPlace(placeFromIntent)
            Log.d("testt", "place from intent: $placeFromIntent")
        }
        Log.d("testt", "null 임")
    }

    private fun startMap(place: PlaceVO) {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("testt", "지도 종료됨")
            }

            override fun onMapError(error: Exception) {
                Log.d("testt", error.message ?: "Unknown error")
                handleError(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("testt", "지도 마커 표시 준비됨")
                this@MapActivity.kakaoMap = kakaoMap
                setCameraPosition(place.latitude, place.longitude)
                addMarker(place)
                displayBottomSheet(place)
                mapViewModel.saveLastPlace(place)
            }
        })
    }

    private fun startDefaultMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                //no-op
            }

            override fun onMapError(error: Exception) {
                handleError(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MapActivity.kakaoMap = kakaoMap
                setCameraPosition(37.5665, 126.9780)
            }
        })
    }

    private fun setCameraPosition(latitude: Double, longitude: Double) {
        val cameraPositionBuilder = CameraPosition.Builder().apply {
            setPosition(LatLng.from(latitude, longitude))
            setZoomLevel(21)
        }

        kakaoMap.moveCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.from(
                    cameraPositionBuilder
                )
            ), CameraAnimation.from(500, true, true)
        )
    }

    private fun addMarker(place: PlaceVO) {
        var styles = LabelStyles.from(
            "marker",
            LabelStyle.from(R.drawable.marker).setZoomLevel(8)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )

        styles = kakaoMap.labelManager!!.addLabelStyles(styles!!)

        val label = kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(LatLng.from(place.latitude, place.longitude))
                .setTexts(place.placeName)
                .setStyles(styles)
        )
    }

    private fun displayBottomSheet(place: PlaceVO) {
        binding.nameTextaView.text = place.placeName
        binding.addressTextView.text = place.addressName
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun handleError(error: Exception) {
        Log.d("testt", error.message ?: "Unknown error")
        runOnUiThread {
            val intent = Intent(this@MapActivity, ErrorActivity::class.java)
            intent.putExtra("error", error.message)
            startActivity(intent)
        }
    }
}
