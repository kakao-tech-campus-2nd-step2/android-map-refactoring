package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

class MapActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    var map: KakaoMap? = null
    var savedLatitude: Double = 37.5642
    var savedLongitude: Double = 127.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        mapView = findViewById(R.id.map_view)
        loadData()  // 저장 위치 가져오기

        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                Log.e("KakaoMap", "onMapError", error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                Log.d("KakaoMap", "onMapReady")
                map = kakaoMap

                val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(savedLatitude, savedLongitude))
                kakaoMap.moveCamera(cameraUpdate)

                // 지도 위치
                val name = intent.getStringExtra("name")
                val address = intent.getStringExtra("address")
                val latitude = intent.getStringExtra("latitude")?.toDouble()
                val longitude = intent.getStringExtra("longitude")?.toDouble()
                if (latitude != null && longitude != null) {
                    val labelManager = kakaoMap.labelManager
                    val iconAndTextStyle = LabelStyles.from(
                        LabelStyle.from(R.drawable.location).setTextStyles(20, Color.BLACK)
                    )
                    val options = LabelOptions.from(LatLng.from(latitude, longitude))
                        .setStyles(iconAndTextStyle)
                    val layer = labelManager?.layer
                    val label = layer?.addLabel(options)
                    label?.changeText(name ?: "Unknown")
                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
                    kakaoMap.moveCamera(cameraUpdate)

                    savedLatitude = latitude
                    savedLongitude = longitude

                }

                val bottomSheetDialog = BottomSheetDialog(this@MapActivity)
                val bottomSheetLayout = layoutInflater.inflate(R.layout.bottom_sheet,null)
                bottomSheetDialog.setContentView(bottomSheetLayout)
                val bottomName = bottomSheetLayout.findViewById<TextView>(R.id.tvBName)
                val bottomAddress = bottomSheetLayout.findViewById<TextView>(R.id.tvBAddress)
                bottomName.text= name
                bottomAddress.text = address
                bottomSheetDialog.show()

            }
        })

        val startMainActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // 나중에 반환된걸로 처리할 게 있으면 사용하기
            }
        }

        etSearch.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startMainActivityForResult.launch(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
        saveData(savedLatitude.toString(), savedLongitude.toString())
    }

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
