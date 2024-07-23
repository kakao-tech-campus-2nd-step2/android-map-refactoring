package campus.tech.kakao.map.presentation.map

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.presentation.search.SearchActivity
import campus.tech.kakao.map.presentation.ViewModelFactory
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
    private val mapView by lazy<MapView> { findViewById(R.id.mapView) }
    private val searchView by lazy<ConstraintLayout> { findViewById(R.id.searchView) }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapBottomSheet: MapBottomSheet
    private lateinit var tvErrorMessage: TextView
    private lateinit var kakaoMap: KakaoMap
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)


        initViewModel()
        initMapView()
        initSearchView()
        setResultLauncher()

    }

    private fun initViewModel(){
        val placeRepository = (application as PlaceApplication).placeRepository
        mapViewModel = ViewModelProvider(this, ViewModelFactory(placeRepository))
            .get(MapViewModel::class.java)

        mapViewModel.lastVisitedPlace.observe(this, { place ->
            place?.let {
                updateMapWithPlaceData(it)
                showBottomSheet(it)
            }
        })
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
                    tvErrorMessage.visibility = View.GONE
                    mapView.visibility = View.VISIBLE
                    mapViewModel.loadLastVisitedPlace()
                }
            }
        })
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
