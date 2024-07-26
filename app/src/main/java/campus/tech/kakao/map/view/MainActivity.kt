package campus.tech.kakao.map.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.viewmodel.MyViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.data.KAKAO_LATITUDE
import campus.tech.kakao.map.model.data.KAKAO_LONGITUDE
import campus.tech.kakao.map.model.data.Location
import campus.tech.kakao.map.util.BottomSheetManager
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MyViewModel by viewModels()  //hilt를 이용하여 viewModel 인스턴스화
    private var mapView: MapView? = null
    private var kakaoMap: KakaoMap? = null
    private val bottomSheet: LinearLayout by lazy { findViewById<LinearLayout>(R.id.bottom_sheet) }

    private lateinit var searchPlaceLauncher: ActivityResultLauncher<Intent>


    private var mainLocation: Location = Location("kakao", "주소") //위도, 경도는 기본값(카카오판교)
    private var cameraUpdate = CameraUpdateFactory.newCenterPosition(
        LatLng.from(
            mainLocation.latitude,
            mainLocation.longitude
        )
    )

//    val bottomSheetManager = BottomSheetManager(this, bottomSheet)

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel

        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this, appKey)

        getSharedPreferences()  //sharedPreference에서 값을 불러와 mainLocation에 저장

        //bottomSheet 초기화

        val bottomSheetManager = BottomSheetManager(this, bottomSheet)
        bottomSheetManager.setBottomSheetText(mainLocation)

        // ActivityResultLauncher 초기화
        searchPlaceLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                mainLocation = Location(
//                    name = data?.getStringExtra("name") ?: "",
//                    address = data?.getStringExtra("address") ?: "",
//                    latitude = data?.getStringExtra("latitude")?.toDoubleOrNull() ?: KAKAO_LATITUDE,
//                    longitude = data?.getStringExtra("longitude")?.toDoubleOrNull() ?: KAKAO_LONGITUDE
//                )
                viewModel.getSharedPreferences()
            }
        }

        //SearchPlaceActivity로 이동
        viewModel.isIntent.observe(this, Observer {
            if (it) {
                val intent = Intent(this@MainActivity, SearchPlaceActivity::class.java)
                searchPlaceLauncher.launch(intent)
            }
        })

        //mainLocation 최신화
        viewModel.location.observe(this, Observer { location ->
            mainLocation = location
//            mapView?.resume()
            bottomSheetManager.setBottomSheetText(location)
            updateCamera()
            addMarker(kakaoMap, location)
        })


        mapView = binding.mapView
        mapView?.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API가 정상적으로 종료될 때 호출
                    Log.d("KakaoMap", "onMapDestroy: ")
                }

                override fun onMapError(error: Exception) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                    Log.d("KakaoMap", "onMapError")
                    Log.e("KakaoMap", "onMapError: ", error)
                    val intent = Intent(this@MainActivity, MapErrorActivity::class.java)
                    intent.putExtra("error", error.toString().substring(20))
                    startActivity(intent)
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    // 정상적으로 인증이 완료되었을 때 호출
                    // KakaoMap 객체를 얻어 옵니다.
                    kakaoMap = map
                    //카메라 업데이트
                    updateCamera()
                    addMarker(map, mainLocation)
                }
            }
        )  //mapView.start
    }   //onCreate

    override fun onResume() {
        super.onResume()
        mapView?.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
        Log.d("KakaoMap", "onMapPause")
    }

    //카메라(화면) 움직이기
    private fun updateCamera() {
        cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(
                mainLocation.latitude,
                mainLocation.longitude
            )
        )    //위도,경도 세팅
        kakaoMap?.moveCamera(cameraUpdate)  //세팅한 좌표로 카메라이동
    }

    //mainLocation에 들어갈 데이터 받아오기
    private fun getSharedPreferences() {
        viewModel.getSharedPreferences()
    }

    private fun addMarker(kakaoMap: KakaoMap?, location: Location) {

        val labelManager = kakaoMap?.labelManager ?: return

        val iconAndTextStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.location) // 이미지 리소스 확인
                .setTextStyles(25, Color.BLACK) // 텍스트 스타일
        )

        val options = LabelOptions.from(LatLng.from(location.latitude, location.longitude))
            .setStyles(iconAndTextStyle)

        val layer = labelManager.layer
        if (layer != null) {
            val label = layer.addLabel(options)
            label.changeText(location.name)
        } else {
            Log.e("AddMarker", "Layer is null")
        }
    }
}
