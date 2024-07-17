package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
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

        intent?.let {
            val latitude = it.getDoubleExtra("mapY", 35.231627)
            val longitude = it.getDoubleExtra("mapX", 129.084020)
            startPosition = LatLng.from(latitude, longitude)
        }

        mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                }

                override fun onMapError(error: Exception) {
                    Log.e("MapActivity", "Map error: ${error.message}")
                }
            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    this@MapActivity.kakaoMap = kakaoMap
                }

                override fun getPosition(): LatLng {
                    Log.d("here", "startPosition: $startPosition")
                    return startPosition
                }
            }
        )

        searchButton.setOnClickListener {
            val mapToSearchIntent = Intent(this@MapActivity, SearchActivity::class.java)
            startActivity(mapToSearchIntent)
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
