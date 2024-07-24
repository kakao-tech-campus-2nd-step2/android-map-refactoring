package campus.tech.kakao.map.presentation.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.search.SearchActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val mapView by lazy<MapView> { findViewById(R.id.mapView) }
    private val searchView by lazy<ConstraintLayout> { findViewById(R.id.searchView) }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapBottomSheet: MapBottomSheet
    private lateinit var tvErrorMessage: TextView
    private lateinit var kakaoMap: KakaoMap
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        observeViewModel()
        initMapView()
        initSearchView()
        setResultLauncher()

    }

    private fun initMapView() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {
                showErrorPage(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                if (!isNetworkAvailable()) {
                    showErrorPage(Exception("네트워크 연결 오류"))
                }else{
                    kakaoMap = map
                    mapViewModel.loadLastVisitedPlace()
                    tvErrorMessage.visibility = View.GONE
                    mapView.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mapViewModel.lastVisitedPlace.collect { place ->
                place?.let {
                    updateMapWithPlaceData(it)
                    showBottomSheet(it)
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        return PlaceApplication.isNetworkActive()
    }

    private fun initSearchView() {
        searchView.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun showErrorPage(error: Exception) {
        tvErrorMessage = findViewById(R.id.tvErrorMessage)
        tvErrorMessage.visibility = View.VISIBLE
        mapView.visibility = View.GONE
        tvErrorMessage.text = "지도 인증에 실패했습니다.\n다시 시도해주세요.\n" + error.message
    }

    private fun setResultLauncher() {
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val placeData = result.data?.getSerializableExtra("placeData") as? Place
                placeData?.let {
                    mapViewModel.saveLastVisitedPlace(it)
                }
            }
        }
    }

    private fun updateMapWithPlaceData(place: Place) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble()), 15
        )
        kakaoMap.moveCamera(cameraUpdate)

        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(LabelStyle.from(R.drawable.icon_location3))
        )
        val options = LabelOptions.from(
            LatLng.from(place.yPos.toDouble(), place.xPos.toDouble())
        ).setStyles(styles)

        val layer = kakaoMap.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun showBottomSheet(place: Place) {
        mapBottomSheet = MapBottomSheet(place)
        mapBottomSheet.show(supportFragmentManager, mapBottomSheet.tag)
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
