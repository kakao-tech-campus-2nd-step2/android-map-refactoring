package campus.tech.kakao.map.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import javax.inject.Inject

class MapViewModel @Inject constructor() : ViewModel(), OnMapReadyCallback {

    val searchQuery = MutableLiveData<String>()
    private val _mapReady = MutableLiveData<GoogleMap>()
    val mapReady: LiveData<GoogleMap> get() = _mapReady

    fun onSearchQuerySubmitted() {
        val query = searchQuery.value ?: return

        mapReady.value?.let { googleMap ->
            searchLocation(query, googleMap)
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (query.isBlank()) return

        mapReady.value?.let { googleMap ->
            searchLocation(query, googleMap)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        _mapReady.value = googleMap
    }

    private fun searchLocation(query: String, googleMap: GoogleMap) {
        // 검색 로직 구현
    }
}




