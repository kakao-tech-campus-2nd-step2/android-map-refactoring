package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapActivity : AppCompatActivity() {
    lateinit var mapView: MapView
    var map: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val etSearch = findViewById<EditText>(R.id.etSearch)
        mapView = findViewById(R.id.map_view)

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
                }
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
    }
}
