package campus.tech.kakao.map.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.utility.BottomSheetHelper
import campus.tech.kakao.map.utility.MapUtility
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.utils.MapUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {

    @Inject
    lateinit var mapUtility: MapUtility

    @Inject
    lateinit var bottomSheetHelper: BottomSheetHelper

    lateinit var mapBinding: ActivityMapBinding
    lateinit var startMainActivityForResult: ActivityResultLauncher<Intent>
    var map: KakaoMap? = null
    var savedLatitude: Double = 37.5642
    var savedLongitude: Double = 127.00
    val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapBinding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        mapBinding.lifecycleOwner = this
        mapBinding.mapViewModel = mapViewModel

        startMainActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
            }
        }

        mapViewModel.searchClickEvent.observe(this, Observer {
            val intent = Intent(this, MainActivity::class.java)
            startMainActivityForResult.launch(intent)
        })
        loadData()  // 저장 위치 가져오기

        mapBinding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MapActivity, ErrorActivity::class.java).apply{
                    putExtra("errorMessage",error.toString())
                }
                startActivity(intent)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("KakaoMap", "onMapReady")
                map = kakaoMap

                mapUtility.camera(kakaoMap, savedLatitude, savedLongitude)

                // 장소 정보 받아옴
                val name = intent.getStringExtra("name")
                val address = intent.getStringExtra("address")
                val latitude = intent.getStringExtra("latitude")?.toDouble()
                val longitude = intent.getStringExtra("longitude")?.toDouble()
                if (latitude != null && longitude != null) {
                    mapUtility.addMarker(kakaoMap, latitude, longitude, name.toString())
                    mapUtility.camera(kakaoMap, latitude, longitude)

                    savedLatitude = latitude
                    savedLongitude = longitude

                    bottomSheetHelper.showBottomSheet(name.toString(), address.toString())
                }
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
        saveData(savedLatitude.toString(), savedLongitude.toString())
    }

    // 위도와 경도 저장, 가져오기
    fun saveData(latitude: String, longitude: String){
        val pref = getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putString("latitude", latitude)
        edit.putString("longitude", longitude)
        edit.apply()
    }

    fun loadData() {
        val pref = getSharedPreferences("pref", 0)
        savedLatitude = pref.getString("latitude", "37.5642")?.toDouble() ?: 37.5642
        savedLongitude = pref.getString("longitude", "127.00")?.toDouble() ?: 127.00
    }
}
