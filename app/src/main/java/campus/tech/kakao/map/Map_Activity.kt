package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng

class Map_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var searchView: SearchView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById(R.id.map_view)
        searchView = findViewById(R.id.search_text)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, Search_Activity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val seoul = LatLng(37.5665, 126.9780) // 서울의 위도와 경도
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10f))
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}


