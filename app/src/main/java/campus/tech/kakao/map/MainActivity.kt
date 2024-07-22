package campus.tech.kakao.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import campus.tech.kakao.map.databinding.ActivityMainBinding
import campus.tech.kakao.map.model.Place
import campus.tech.kakao.map.viewModel.MapRepository
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.KakaoMapSdk
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.Transition
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kakaoMap: KakaoMap
    private lateinit var marker: Bitmap
    private var label: Label? = null
    private lateinit var styles: LabelStyles

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val bottomSheetLayout by lazy { findViewById<ConstraintLayout>(R.id.bottom_sheet_layout) }
    private val bottomSheetName by lazy { findViewById<TextView>(R.id.name) }
    private val bottomSheetAddress by lazy { findViewById<TextView>(R.id.address) }
    private val bottomSheetCategory by lazy { findViewById<TextView>(R.id.category) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //KakaoMapSdk.init(this, "I'm nativeKey")     // 오류확인

        Log.d("onCreate", "")
        val lastPos = MapRepository(this).getLastPos()
        drawMap(lastPos)
        initBottomSheet()

        binding.searchInput.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

        binding.searchButton.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun drawMap(latLng: LatLng?) {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Log.d("KakaoMap", "카카오맵 종료")
            }

            override fun onMapError(e: Exception?) {
                Log.e("KakaoMap", "카카오맵 인증실패", e)
                setContentView(R.layout.map_error)
                val errorMessage = findViewById<TextView>(R.id.errorMessage)
                errorMessage.text = e?.message
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                Log.d("KakaoMap", "카카오맵 실행")
                kakaoMap = p0
                makeLabelStyle()
//          addLabel(Place("dd", "xxx", "", "127.115587", "37.406960"))
            }

            override fun getPosition(): LatLng {
                return latLng ?: return super.getPosition()
            }
        })
    }

    private fun addLabel(place: Place) {
        val latLng = LatLng.from(place.latitude.toDouble(), place.longitude.toDouble())
        moveCamera(latLng)
        label = kakaoMap.labelManager!!.layer!!.addLabel(
            LabelOptions.from(latLng)
                .setStyles(styles).setTexts(place.name)
        )
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
        Log.d("MainAct State", "Intent is: $intent")
        Log.d("kakaomap", "moveCamera: $kakaoMap")
        kakaoMap?.moveCamera(cameraUpdate, CameraAnimation.from(500, true, true))
    }

    private fun makeLabelStyle() {
        vectorToBitmap(R.drawable.pin_drop_24px)

        styles = LabelStyles.from(
            "myLabel",
            LabelStyle.from(marker)
                .setTextStyles(32, Color.BLACK, 1, Color.GRAY)
                .setZoomLevel(kakaoMap!!.minZoomLevel)
        )
        styles = kakaoMap.labelManager?.addLabelStyles(styles)!!
    }

    private fun vectorToBitmap(drawableId: Int) {
        val drawable = ContextCompat.getDrawable(this, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable!!.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        marker = bitmap
    }

    private fun showBottomSheet(place: Place) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetName.text = place.name
        bottomSheetAddress.text = place.address
        bottomSheetCategory.text = place.category
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.searchWindow.isInvisible = (newState < BottomSheetBehavior.STATE_COLLAPSED)
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {
            }
        })
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
        Log.d("MainAct State", "Intent is: $intent")

    }

    override fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent?.let {
            var place: Place?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                place = intent.getParcelableExtra("place", Place::class.java)
            } else {
                place = intent.getParcelableExtra("place") as Place?
            }
            place?.let {
                kakaoMap.labelManager!!.layer!!.remove(label)
                addLabel(place)
                showBottomSheet(place)
            }
        }
    }
}
