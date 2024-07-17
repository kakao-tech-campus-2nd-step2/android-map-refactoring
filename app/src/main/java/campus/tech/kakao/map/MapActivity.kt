package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
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

    private lateinit var mapView: MapView
    private lateinit var searchButton: LinearLayout
    private lateinit var startPosition: LatLng

    private var latitude: Double = 35.231627
    private var longitude: Double = 129.084020
    private var name: String? = null
    private var kakaoMap: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchButton = findViewById(R.id.search_button)

        intent?.let {
            latitude = it.getDoubleExtra("mapY", latitude)
            longitude = it.getDoubleExtra("mapX", longitude)
            name = it.getStringExtra("name")
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
                    name?.let {
                        addMarker(kakaoMap, latitude, longitude, it)
                    }
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

    private fun addMarker(kakaoMap: KakaoMap, latitude: Double, longitude:Double, name: String) {
        Log.d("addMarker", "addMarker: $latitude, $longitude, $name")
        val styles = kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.location_marker).setTextStyles(25,
            Color.BLACK)))
        val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles)
        val layer = kakaoMap.labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)
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
