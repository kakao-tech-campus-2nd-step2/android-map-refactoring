package campus.tech.kakao.map.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.viewmodel.PlaceViewModel
import com.kakao.sdk.common.util.Utility
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapAuthException
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_PLACE_LONGITUDE = "extra_place_longitude"
        const val EXTRA_PLACE_LATITUDE = "extra_place_latitude"
        const val EXTRA_PLACE_NAME = "extra_place_name"
        const val EXTRA_PLACE_ADDRESSNAME = "extra_place_addressname"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var mapView: MapView
    private lateinit var kakaoMap: KakaoMap
    private val viewModel: PlaceViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initKakaoMap()
        initSearchEditText()
        initMapView()

    }

    private fun initKakaoMap() {
        val keyHash = Utility.getKeyHash(this)
        Log.d("testt", keyHash)
        val key = getString(R.string.kakao_api_key)
        KakaoMapSdk.init(this, key)
    }

    private fun initSearchEditText() {
        binding.searchEditText.isFocusable = false
        binding.searchEditText.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initMapView() {
        mapView = binding.mapView
        mapView.start(object : MapLifeCycleCallback() {

            override fun onMapDestroy() {
                Log.d("testt", "onMapDestory")
            }

            override fun onMapError(error: Exception) {
                val errorMessage = when ((error as? MapAuthException)?.errorCode) {
                    401 -> "API 인증에 실패"
                    499 -> "서버 통신 실패"
                    else -> "알 수 없는 오류"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, ErrorActivity::class.java)
                intent.putExtra("error", errorMessage)
                startActivity(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                this@MainActivity.kakaoMap = kakaoMap
                setCameraPosition()
                setMarker()
                setBottomSheet()

            }
        })
    }

    private fun setCameraPosition() {
        // LiveData 관찰
        viewModel.placeData.observe(this, Observer { placeData ->
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(placeData.latitude, placeData.longitude))
            kakaoMap.moveCamera(cameraUpdate)
        })
    }

    private fun setMarker() {

        // LiveData 관찰
        viewModel.placeData.observe(this, Observer { placeData ->
            val styles = LabelStyles.from(
                LabelStyle.from(R.drawable.marker_128).setZoomLevel(10)
                    .setTextStyles(LabelTextStyle.from(32, Color.parseColor("#000000")))
            ).let {
                kakaoMap.labelManager?.addLabelStyles(it)
            }

            kakaoMap.labelManager?.layer?.addLabel(
                LabelOptions.from(LatLng.from(placeData.latitude, placeData.longitude))
                    .setStyles(styles)
                    .setTexts(placeData.placeName)
            )
        })
    }

    private fun setBottomSheet() {

        // 데이터 로드
        viewModel.placeData.observe(this, Observer { placeData ->
            binding.bottomSheetTitle.text = placeData.placeName
            binding.bottomSheetDescription.text = placeData.addressName
        })
    }

    override fun onResume() {
        super.onResume()
        Log.d("testt", "onResume")
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        Log.d("testt", "onPause")
        mapView.pause()
    }
}
