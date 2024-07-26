package campus.tech.kakao.map.view.map

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.model.state.UiState
import campus.tech.kakao.map.view.ErrorView
import campus.tech.kakao.map.view.search.SearchActivity
import campus.tech.kakao.map.viewmodel.map.MapViewModel
import campus.tech.kakao.map.viewmodel.map.MapViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelTextStyle
import com.kakao.vectormap.label.LabelTransition
import com.kakao.vectormap.label.Transition
import java.lang.Exception

class MapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapViewModel: MapViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var errorView: ErrorView? = null
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var map: KakaoMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        initView()
        setupViewModel()
        checkLocationPermission()
        setupMapView()
        setupSearchClickListener()
        observeUiState()
        addActivityResultLauncher()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    private fun setupBinding() {
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    private fun initView() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet.root)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupViewModel() {
        mapViewModel = ViewModelProvider(this, MapViewModelFactory(this))[MapViewModel::class.java]
    }

    private fun checkLocationPermission(): Boolean {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        kotlin.runCatching {
            if (
                ActivityCompat.checkSelfPermission(
                    this,
                    locationPermissions[0]
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    locationPermissions[1]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    locationPermissions,
                    100
                )
                return false
            } else {
                return true
            }
        }.onFailure {
            Log.e("MapActivity", "locationError : ${it.message}}")
        }
        return false
    }

    private fun setupMapView(position: LatLng? = null) {
        binding.mapView.start(
            object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API 가 정상적으로 종료될 때 호출됨
                }

                override fun onMapError(error: Exception?) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                    error?.let {
                        mapViewModel.showErrorView(it)
                    }
                }

            },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(kakaoMap: KakaoMap) {
                    // 인증 후 API 가 정상적으로 실행될 때 호출됨
                    mapViewModel.showSuccessView()
                    map = kakaoMap.apply {
                        setOnCameraMoveEndListener { _, cameraPosition, _ ->
                            mapViewModel.updateLastPosition(
                                cameraPosition.position.latitude,
                                cameraPosition.position.longitude
                            )
                        }
                    }
                }

                override fun getPosition(): LatLng {
                    mapViewModel.readLastPosition().first?.let { latitude ->
                        mapViewModel.readLastPosition().second?.let { longitude ->
                            return LatLng.from(latitude, longitude)
                        }
                    }
                    return position ?: super.getPosition()
                }
            }
        )
    }

    private fun setupSearchClickListener() {
        binding.flowSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            launcher?.launch(intent)
        }
    }

    private fun observeUiState() {
        mapViewModel.uiState.observe(this) {
            when (it) {
                is UiState.Error -> showMapAuthErrorView(it.e)
                UiState.Success -> hideErrorView()
                UiState.Loading -> {
                    /** do nothing **/
                }

                UiState.NotInitialized -> {
                    /** do nothing **/
                }
            }
        }
    }

    private fun showMapAuthErrorView(e: Exception) {
        errorView = ErrorView(
            context = this,
            errorMessage = getString(R.string.map_auth_error).plus("\n\n${e.message}"),
            retry = { setupMapView() }
        )
        if (errorView?.isAttachedToWindow == false) {
            (binding.root as? ViewGroup)?.apply {
                removeView(errorView)
                addView(
                    errorView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            }
        }
    }

    private fun hideErrorView() {
        if (errorView?.isAttachedToWindow == true) {
            (binding.root as? ViewGroup)?.apply {
                removeView(errorView)
            }
        }
    }

    private fun addActivityResultLauncher() {
        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val location: LocalUiModel =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            result.data?.getParcelableExtra(
                                "selected_location",
                                LocalUiModel::class.java
                            )
                        } else {
                            result.data?.getParcelableExtra("selected_location")
                        } ?: return@registerForActivityResult

                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    binding.bottomSheet.tvPlaceName.text = location.place
                    binding.bottomSheet.tvCategory.text = location.category
                    binding.bottomSheet.tvAddress.text = location.address

                    val position = LatLng.from(location.latitude, location.longitude)
                    map?.apply {
                        moveToLocation(position, 15)
                        addLabel(position, location.place)
                    }
                }
            }
    }

    private fun KakaoMap.moveToLocation(position: LatLng, zoomLevel: Int = 0) {
        val focusZoomLevel = if (zoomLevel == 0) this.zoomLevel else zoomLevel

        moveCamera(
            CameraUpdateFactory.newCenterPosition(position, focusZoomLevel),
            CameraAnimation.from(100)
        )
    }

    private fun KakaoMap.addLabel(position: LatLng, placeName: String) {
        val label = labelManager?.layer?.addLabel(
            LabelOptions.from("location", position)
                .setStyles(
                    LabelStyle.from(R.drawable.red_marker)
                        .setIconTransition(LabelTransition.from(Transition.Scale, Transition.Scale))
                        .setTextStyles(
                            LabelTextStyle.from(30, Color.parseColor("#000000"))
                        )
                )
        ) ?: return

        label.changeText(placeName)

        setOnLabelClickListener { _, _, _ ->
            moveToLocation(label.position)
        }

        label.moveTo(position)
    }
}

