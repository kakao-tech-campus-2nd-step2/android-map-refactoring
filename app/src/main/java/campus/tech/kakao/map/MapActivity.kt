package campus.tech.kakao.map

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import campus.tech.kakao.map.databinding.ActivityMapBinding
import campus.tech.kakao.map.databinding.BottomSheetBinding
import campus.tech.kakao.map.databinding.MapErrorBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
	private var map: KakaoMap? = null
	private lateinit var model: MainViewModel
	lateinit var placeName:String
	lateinit var addressName:String
	private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
	private var longitude:Double = 0.0
	private var latitude:Double = 0.0
	private lateinit var binding: ActivityMapBinding
	private lateinit var bottomBinding: BottomSheetBinding
	companion object{
		const val MARKER_WIDTH = 100
		const val MARKER_HEIGHT = 100
		const val MARKER_TEXT_SIZE = 40
		const val ZOOM_LEVEL = 17
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		model = ViewModelProvider(this)[MainViewModel::class.java]
		getMapInfo()
		binding = ActivityMapBinding.inflate(layoutInflater)
		binding.map = this
		val view = binding.root
		setContentView(view)
		bottomBinding = BottomSheetBinding.inflate(layoutInflater)
		model.documentClickedDone()
		binding.mapView.start(object : MapLifeCycleCallback() {
			override fun onMapDestroy() {

			}

			override fun onMapError(p0: Exception?) {
				val errorBinding = MapErrorBinding.inflate(layoutInflater)
				setContentView(errorBinding.root)
				errorBinding.mapErrorText.text = p0?.message
			}

		}, object: KakaoMapReadyCallback() {
			override fun onMapReady(kakaoMap: KakaoMap) {
				map = kakaoMap
			}

			override fun getPosition(): LatLng {
				return LatLng.from(latitude, longitude)
			}

			override fun getZoomLevel(): Int {
				return ZOOM_LEVEL
			}
		})
		binding.searchBar.setOnClickListener {
			onSearchBarClicked()
		}
		initBottomSheet()
		documentClickedObserve()
	}

	private fun onSearchBarClicked(){
		val fragmentManager = supportFragmentManager
		val searchFragment = SearchFragment()
		val transaction = fragmentManager.beginTransaction()
		transaction.replace(binding.activityMap.id, searchFragment)
		transaction.addToBackStack(null)
		binding.activityMapFrameLayout.setOnTouchListener(View.OnTouchListener { v, event -> true })
		transaction.commit()
	}

	override fun onResume() {
		super.onResume()
		binding.mapView.resume()
	}
	private fun documentClickedObserve(){
		model.documentClicked.observe(this){documentClicked->
			if(documentClicked){
				makeMarker()
				setBottomSheet()
				val cameraUpdate: CameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
				map?.moveCamera(cameraUpdate)
			}
			else{
				map?.labelManager?.layer?.removeAll()
				bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
			}
		}
	}

	override fun onPause() {
		super.onPause()
		binding.mapView.pause()
	}

	private fun getMapInfo(){
		model.getMapInfo()
		model.mapInfo.observe(this) { mapInfo ->
			if (!mapInfo.isNullOrEmpty()) {
				latitude = mapInfo[0].toDouble()
				longitude = mapInfo[1].toDouble()
				placeName = mapInfo[2]
				addressName = mapInfo[3]
			}
			else{
				Log.e("testt", "getMapInfo: mapInfo is null")
			}
		}
	}

	private fun makeMarker(){
		val bitmapImage = BitmapFactory.decodeResource(resources, R.drawable.marker)
		val markerImage = Bitmap.createScaledBitmap(bitmapImage, MARKER_WIDTH, MARKER_HEIGHT, true)
		val styles = map?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(markerImage).setTextStyles(
			MARKER_TEXT_SIZE, Color.BLACK)))
		if(styles != null){
			val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles).setTexts(placeName)
			map?.labelManager?.layer?.removeAll()
			map?.labelManager?.layer?.addLabel(options)
		}
		else{
			Log.e("MapActivity", "makeMarker: styles is null")
		}
	}

	private fun initBottomSheet(){
		bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetInclude.bottomSheet)
		bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
			override fun onStateChanged(bottomSheet: View, newState: Int) {
			}

			override fun onSlide(bottomSheet: View, slideOffset: Float) {
			}

		})
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
	}

	private fun setBottomSheet(){
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
		binding.invalidateAll()
	}
	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		if (keyCode == KeyEvent.KEYCODE_BACK && supportFragmentManager.backStackEntryCount > 0) {
			model.documentClickedDone()
			supportFragmentManager.popBackStack()
			return true
		}

		return super.onKeyDown(keyCode, event)
	}
}