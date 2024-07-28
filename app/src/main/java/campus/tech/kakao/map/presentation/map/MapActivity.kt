package campus.tech.kakao.map.presentation.map

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import campus.tech.kakao.map.PlaceApplication
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
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
    private lateinit var binding: ActivityMapBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var mapBottomSheet: MapBottomSheet
    private lateinit var kakaoMap: KakaoMap
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarTransparent()
        initBinding()
        collectViewModel()
        initSwipeRefreshLayout()
        initMapView()
        initSearchView()
        setResultLauncher()
    }

    private fun setStatusBarTransparent() {
        this.window?.apply {
            this.statusBarColor = Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun initBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this
        binding.viewModel = mapViewModel

    }

    private fun collectViewModel() {
        lifecycleScope.launch {
            mapViewModel.lastVisitedPlace.collect { place ->
                place?.let {
                    updateMapWithPlaceData(it)
                    showBottomSheet(it)
                }
            }
        }
    }
    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            if (!isNetworkAvailable()) {
                showErrorPage(Exception("네트워크 연결 오류"))
            }else{
                showMapPage()
                showBottomSheet(mapViewModel.lastVisitedPlace.value)
                binding.swipeRefreshLayout.isEnabled = false
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun initMapView() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(error: Exception) {
                showErrorPage(error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                kakaoMap = map
                if (!isNetworkAvailable()) {
                    showErrorPage(Exception("네트워크 연결 오류"))
                }else{
                    binding.swipeRefreshLayout.isEnabled = false
                    initMapPage()
                }
            }
        })
    }

    private fun isNetworkAvailable(): Boolean {
        return PlaceApplication.isNetworkActive()
    }

    private fun initMapPage(){
        showMapPage()
        mapViewModel.loadLastVisitedPlace()
    }

    private fun showMapPage(){
        binding.tvErrorMessage.visibility = View.GONE
        binding.searchView.visibility = View.VISIBLE
        binding.mapView.visibility = View.VISIBLE
    }

    private fun showErrorPage(error: Exception) {
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.mapView.visibility = View.GONE
        binding.searchView.visibility = View.GONE
        binding.tvErrorMessage.text = "지도 인증에 실패했습니다.\n다시 시도해주세요.\n" + error.message
    }

    private fun initSearchView() {
        binding.searchView.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            resultLauncher.launch(intent)
        }
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

    private fun showBottomSheet(place: Place?) {
        val bottomSheet = MapBottomSheet()
        place?.let {
            val args = Bundle()
            args.putSerializable("place", it)
            bottomSheet.arguments = args }
        bottomSheet.show(supportFragmentManager, bottomSheet.tag)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }
}
