package campus.tech.kakao.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar

class Map_Activity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var searchView: SearchView
    private lateinit var googleMap: GoogleMap
    val placeViewModel: PlaceViewModel by viewModels() //이걸 추가를 안해줘서 에러가 난거였음!!

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

        val selectedPlace = intent.getParcelableExtra<Place>("selectedPlace")

        placeViewModel.getLastKnownLocation()?.let { lastLocation ->
            val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        }

        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        }
    }
    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { placeViewModel.searchAddress(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun observeViewModel() {
        placeViewModel.searchResult.observe(this, { place ->
            place?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            }
        })

        placeViewModel.errorMessage.observe(this, { errorMessage ->
            errorMessage?.let {
                Snackbar.make(mapView, errorMessage, Snackbar.LENGTH_LONG).show()
            }
        })
    }
    private fun showMapErrorLayout() {
        // Hide the map view
        mapView.visibility = View.GONE

        // Show the error layout
        val errorLayout = findViewById<TextView>(R.id.error_message)
        val errorLayout1 = findViewById<TextView>(R.id.error_message1)
        errorLayout.visibility = View.VISIBLE
        errorLayout1.visibility = View.VISIBLE

        // Retry button click listener
        val retryButton = findViewById<Button>(R.id.retry_button)
        retryButton.setOnClickListener {
            // Retry logic here, for example:
            mapView.visibility = View.VISIBLE
            errorLayout.visibility = View.GONE
            mapView.onResume() // Resume map operations
        }
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


