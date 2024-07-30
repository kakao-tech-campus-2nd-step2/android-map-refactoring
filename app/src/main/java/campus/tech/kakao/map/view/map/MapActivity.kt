package campus.tech.kakao.map.view.map

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.databinding.ErrorMapBinding
import campus.tech.kakao.map.databinding.MapBottomSheetBinding
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.view.search.MainActivity
import campus.tech.kakao.map.viewmodel.LocationViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var activityMapBinding: ActivityMapBinding
    private lateinit var errorMapBinding: ErrorMapBinding
    private lateinit var mapBottomSheetBinding: MapBottomSheetBinding

    companion object{
        private const val DEFAULT_LONGITUDE = 127.115587
        private const val DEFAULT_LATITUDE = 37.406960
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMapBinding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(activityMapBinding.root)

        errorMapBinding = ErrorMapBinding.inflate(layoutInflater)
        mapBottomSheetBinding = activityMapBinding.mapBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(mapBottomSheetBinding.bottomSheetLayout)

        setupEditText()
        setupMapView()
    }

    override fun onResume() {
        super.onResume()
        activityMapBinding.mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        activityMapBinding.mapView.pause() // MapView 의 pause 호출
    }

    private fun setupEditText() {
        activityMapBinding.searchEditTextInMap.setOnClickListener {
            val intent: Intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMapView() {
        activityMapBinding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("jieun", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {  // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("jieun", "onMapError$error")
                showErrorMessage(error)
            }
        }, object : KakaoMapReadyCallback() {
            val location = getLocation()
            override fun onMapReady(kakaoMap: KakaoMap) { // 인증 후 API 가 정상적으로 실행될 때 호출됨
                Log.d("jieun", "onMapReady location: " + location.toString())
                if (location != null) {
                    showLabel(location, kakaoMap)
                    showBottomSheet(location)
                    locationViewModel.addLastLocation(location)
                } else{
                    hideBottomSheet()
                }
            }

            override fun getPosition(): LatLng {
                if (location != null) {
                    return LatLng.from(location.latitude, location.longitude)
                } else{
                    return LatLng.from(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
                }

            }

        })
    }

    private fun showErrorMessage(error: Exception) {
        runOnUiThread {
            setContentView(errorMapBinding.root)
            errorMapBinding.errorMessageTextView.text = "지도 인증을 실패했습니다.\n다시 시도해주세요.\n\n" + error.message
        }
    }

    private fun showLabel(
        location: Location,
        kakaoMap: KakaoMap
    ) {
        val labelStyles: LabelStyles = LabelStyles.from(
            LabelStyle.from(R.drawable.location_red_icon_resized).setZoomLevel(8),
            LabelStyle.from(R.drawable.location_red_icon_resized)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15)
        )
        val position = LatLng.from(location.latitude, location.longitude)
        kakaoMap.labelManager?.layer?.addLabel(
            LabelOptions.from(position)
                .setStyles(labelStyles)
                .setTexts(location.title)
        )
    }

    private fun hideBottomSheet() {
        mapBottomSheetBinding.bottomSheetLayout.visibility = View.GONE
    }

    private fun showBottomSheet(location: Location) {
        mapBottomSheetBinding.bottomSheetLayout.visibility = View.VISIBLE
        mapBottomSheetBinding.bottomSheetTitle.text = location.title
        Log.d("jieun", "mapBottomSheetBinding.bottomSheetTitle.text:"+mapBottomSheetBinding.bottomSheetTitle.text)
        mapBottomSheetBinding.bottomSheetAddress.text = location.address
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun getLocation(): Location? {
        var location = getLocationByIntent()
        if(location == null) {
            location = locationViewModel.getLastLocation()
        }
        return location

    }

    private fun getLocationByIntent(): Location? {
        if (intent.hasExtra("location")) {
            val location = intent.getParcelableExtra("location", Location::class.java) // API 레벨 오류, 실행에는 문제없다.
            Log.d("jieun","getLocationByIntent location "+location.toString())
            return location
        }
        return null
    }
}