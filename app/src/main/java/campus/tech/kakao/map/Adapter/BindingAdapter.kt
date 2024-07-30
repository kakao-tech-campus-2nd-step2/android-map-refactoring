package campus.tech.kakao.map.Adapter

import androidx.databinding.BindingAdapter
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback

@BindingAdapter("mapReady")
fun bindMapReady(mapView: MapView, callback: OnMapReadyCallback?) {
    callback?.let {
        mapView.getMapAsync(it)
    }
}

