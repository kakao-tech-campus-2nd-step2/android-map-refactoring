package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.*
import com.kakao.vectormap.camera.CameraUpdateFactory

class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var searchButton: LinearLayout
    private lateinit var startPosition: LatLng
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchButton = findViewById(R.id.search_button)

        // Intent로부터 좌표를 받아 LatLng 객체를 생성합니다.
        intent?.let {
            val mapX = it.getDoubleExtra("mapX", 35.231627)
            val mapY = it.getDoubleExtra("mapY", 129.084020)
            startPosition = LatLng.from(mapX, mapY)
            Log.d("MapActivity", "Received coordinates: $mapX, $mapY")
        }

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {}
                override fun onMapError(error: Exception) {}
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    this@MapActivity.kakaoMap = kakaoMap
                    moveMapToPosition(startPosition)  // 지도 준비가 완료되면 초기 위치로 이동합니다.
                }

                override fun getPosition(): LatLng {
                    return startPosition
                }
            }
        )

        searchButton.setOnClickListener {
            val mapToSearchIntent = Intent(this@MapActivity, SearchActivity::class.java)
            startActivity(mapToSearchIntent)
        }
    }

    private fun moveMapToPosition(position: LatLng) {
        kakaoMap?.let {
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(position)
            it.moveCamera(cameraUpdate)
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
