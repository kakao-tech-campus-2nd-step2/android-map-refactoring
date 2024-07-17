package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.databinding.ActivityMainBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var viewModel : MyViewModel
    var mapView: MapView? = null
    var kakaoMap: KakaoMap? = null
    val KAKAO_LATITUDE : Double =37.39571538711179
    val KAKAO_LONGITUDE : Double = 127.11051285266876
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        binding.viewModel = viewModel

        val appKey = BuildConfig.KAKAO_API_KEY
        KakaoMapSdk.init(this, appKey)

        //SearchPlaceActivity로 이동
        viewModel.isIntent.observe(this, Observer {
            if(it) {
                val intent = Intent(this@MainActivity, SearchPlaceActivity::class.java)
                startActivity(intent)
            }
        })
        
        //sharedPreference에서 name,address,longitude,latitude 받아서 변수에 저장하기
        val sharedPreferences = getSharedPreferences("PlacePreferences", MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "")
        val address = sharedPreferences.getString("address", "")
        val longitude = sharedPreferences.getString("longitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LONGITUDE
        val latitude = sharedPreferences.getString("latitude", "0.0")?.toDoubleOrNull() ?: KAKAO_LATITUDE


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
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    // 정상적으로 인증이 완료되었을 때 호출
                    // KakaoMap 객체를 얻어 옵니다.
                    kakaoMap = map



                    // 마지막 위치 받아와서 카메라 이동하기 구현하기!!


                    var cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))

                    kakaoMap?.moveCamera(cameraUpdate)
                    Log.d("KakaoMap", "onMapReady")
                    Log.d("KakaoMap", "name : $name")
                    Log.d("KakaoMap", "address : $address")
                    Log.d("KakaoMap", "longitude : $longitude")
                    Log.d("KakaoMap", "latitude : $latitude")
                }
            }
        )  //mapView.start

    }   //onCreate

    override fun onResume() {
        super.onResume()
        mapView?.resume()
        Log.d("KakaoMap", "onMapResume")
    }

    override fun onPause() {
        super.onPause()
        mapView?.pause()
        Log.d("KakaoMap", "onMapPause")
    }

}
