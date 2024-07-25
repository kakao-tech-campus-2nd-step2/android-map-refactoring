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
import campus.tech.kakao.map.model.Location
import campus.tech.kakao.map.model.datasource.LastLocationlSharedPreferences
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
import javax.inject.Inject

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val locationViewModel: LocationViewModel by viewModels()

    private val searchEditText by lazy { findViewById<EditText>(R.id.SearchEditTextInMap) }
    private val mapView by lazy { findViewById<MapView>(R.id.map_view) }
    private val bottomSheetLayout by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) }
    private val bottom_sheet_title by lazy { findViewById<TextView>(R.id.bottom_sheet_title) }
    private val bottom_sheet_address by lazy { findViewById<TextView>(R.id.bottom_sheet_address) }
    private val errorMessageTextView by lazy { findViewById<TextView>(R.id.errorMessageTextView) }
    private val bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout> by lazy { BottomSheetBehavior.from(bottomSheetLayout) }

    companion object{
        private val DEFAULT_LONGITUDE = 127.115587
        private val DEFAULT_LATITUDE = 37.406960
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        setupEditText()
        setupMapView()
    }

    override fun onResume() {
        super.onResume()
        mapView.resume() // MapView 의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        mapView.pause() // MapView 의 pause 호출
    }

    private fun setupEditText() {
        searchEditText.setOnClickListener {
            val intent: Intent = Intent(this@MapActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupMapView() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("jieun", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {  // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("jieun", "onMapError" + error)
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
//                Log.d("jieun", "getPosition coordinates: " + coordinates.toString())
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
            setContentView(R.layout.error_map)
            errorMessageTextView.text = "지도 인증을 실패했습니다.\n다시 시도해주세요.\n\n" + error.message
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
        kakaoMap.labelManager?.getLayer()?.addLabel(
            LabelOptions.from(position)
                .setStyles(labelStyles)
                .setTexts(location.title)
        )
    }

    private fun hideBottomSheet() {
        bottomSheetLayout.visibility = View.GONE
    }

    private fun showBottomSheet(location: Location) {
        bottomSheetLayout.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottom_sheet_title.text = location.title
        bottom_sheet_address.text = location.address
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
            val location = intent.getSerializableExtra("location") as Location
            Log.d("jieun","getLocationByIntent location "+location.toString())
            return location
        }
        return null
    }
}