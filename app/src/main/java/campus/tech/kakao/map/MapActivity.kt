package campus.tech.kakao.map

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelManager
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles


class MapActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var etSearch: EditText
    private val startZoomLevel = 15
    private var savedLatitude: String = "35.234"
    private var savedLongitude: String ="129.0807"
    private val startPosition = LatLng.from(savedLatitude.toDouble(), savedLongitude.toDouble())
    private var latitude: String = startPosition.latitude.toString()
    private var longitude: String = startPosition.longitude.toString()
    private lateinit var labelManager: LabelManager

    // 지도가 정상적으로 시작된 후 수신
    private val readyCallback: KakaoMapReadyCallback = object : KakaoMapReadyCallback() {
        override fun onMapReady(kakaoMap: KakaoMap) {
            labelManager = kakaoMap.labelManager!!

            // 데이터 받아오기
            latitude = intent.getStringExtra("latitude").toString()
            longitude = intent.getStringExtra("longitude").toString()

            // 선택한 목록 위치 보여주기
            val pos = LatLng.from(longitude.toDouble(), latitude.toDouble())
            val yellowMarker = labelManager.addLabelStyles(
                LabelStyles.from("yellowMarker", LabelStyle.from(R.drawable.yellow_marker))
            )
            labelManager.layer!!.addLabel(
                LabelOptions.from("label", pos)
                    .setStyles(yellowMarker)
            )

            val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(longitude.toDouble(), latitude.toDouble()))
            kakaoMap.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))

            // 모달 창 띄우기
            val name = intent.getStringExtra("name").toString()
            val address = intent.getStringExtra("address").toString()
            val dataBundle = Bundle().apply {
                putString("name", name)
                putString("address", address)
            }
            val modal = ModalBottomSheet()
            modal.arguments = dataBundle
            modal.show(supportFragmentManager, modal.tag)

        }

        override fun getPosition(): LatLng {
            return startPosition
        }

        override fun getZoomLevel(): Int {
            return startZoomLevel
        }

    }

    // 지도의 LifeCycle 관련 이벤트 수신
    private val lifeCycleCallback: MapLifeCycleCallback = object : MapLifeCycleCallback() {

        override fun onMapDestroy() {
            Toast.makeText(
                applicationContext, "onMapDestroy",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onMapError(error: Exception) {
            setContentView(R.layout.error_layout)
            val tvError = findViewById<TextView>(R.id.tvError)
            val btnRefresh = findViewById<ImageButton>(R.id.btnRefresh)
            tvError.text = "지도 인증을 실패했습니다.\n 다시 시도해주세요.\n ${error.message}"
            btnRefresh.setOnClickListener {
                initializeMap()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_layout)
        mapView = findViewById(R.id.mapView)
        etSearch = findViewById(R.id.etSearch)
        mapView.start(lifeCycleCallback, readyCallback)
        etSearch.setOnClickListener {
            val searchIntent = Intent(this, PlaceActivity::class.java)
            startActivity(searchIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        mapView.pause()
    }

    private fun initializeMap() {
        mapView.start(lifeCycleCallback, readyCallback)
    }
}