package campus.tech.kakao.map.Presenter.View

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.Base.ErrorEnum
import campus.tech.kakao.map.Domain.VO.Place
import campus.tech.kakao.map.Presenter.ViewModel.MapViewModel
import campus.tech.kakao.map.R
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


@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var searchText: TextView
    private lateinit var bottomSheet : ConstraintLayout
    private lateinit var placeName : TextView
    private lateinit var placeAddress : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapView = findViewById<MapView>(R.id.mapView)
        searchText = findViewById<TextView>(R.id.search)
        bottomSheet = findViewById<ConstraintLayout>(R.id.bottomSheet)
        placeName = findViewById<TextView>(R.id.placeName)
        placeAddress = findViewById<TextView>(R.id.placeAddress)

        loadStateFromSharedPreference()
        startMap()
        setSearchListener(makeResultLauncher())
        viewModel.currentPlace.observe(this){
            it?.let{
                settingBottomSheet(it)
                saveStateOnSharedPreference(it.id)
            }
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

    private fun loadStateFromSharedPreference(){
        val sharedPreferences = getSharedPreferences(CURRENT_PLACE, Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt(COLUMN_ID, NO_ID)
        if(id != NO_ID) viewModel.initPlace(id)
    }

    private fun startMap(){
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MapActivity,ErrorActivity::class.java)
                intent.putExtra("type", ErrorEnum.MAP_LOAD_ERROR)
                intent.putExtra("msg",error.message)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }, object : KakaoMapReadyCallback() {

            override fun onMapReady(kakaoMap: KakaoMap) {
                handleViewOnStateChanged(kakaoMap)
            }
        })
    }

    private fun handleViewOnStateChanged(kakaoMap: KakaoMap){
        viewModel.currentPlace.observe(this@MapActivity){
            it?.let{
                val latLng = LatLng.from(it.y,it.x)
                addPinOnMap(kakaoMap,latLng,it.name)
                kakaoMap.moveCamera(
                    CameraUpdateFactory.newCenterPosition(latLng)
                )
            }
        }
    }

    private fun settingBottomSheet(place: Place){
        bottomSheet.visibility = VISIBLE
        placeName.text = place.name
        placeAddress.text = place.address
    }

    private fun saveStateOnSharedPreference(id: Int){
        val sharedPreferences = getSharedPreferences(CURRENT_PLACE, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt(COLUMN_ID,id)
        editor.commit()
    }

    private fun addPinOnMap(kakaoMap: KakaoMap, latLng: LatLng, name : String?) {
        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.location_pin)
                .setTextStyles(LABEL_TEXT_SIZE,Color.BLACK)
                .setZoomLevel(LABEL_ZOOM_LEVEL)
            )
        )
        val options = LabelOptions.from(latLng).setStyles(styles).setTexts(name)
        val layer = kakaoMap.labelManager?.layer

        layer?.addLabel(options)
    }

    private fun setSearchListener(startActivityLauncher : ActivityResultLauncher<Intent>){
        searchText.setOnClickListener {
            startActivityLauncher.launch(Intent(this,PlaceSearchActivity::class.java))
        }
    }

    private fun makeResultLauncher() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        when(it.resultCode){
            RESULT_OK -> {
                it.data?.extras?.getInt(PlaceSearchActivity.INTENT_ID)?.let{
                    viewModel.initPlace(it)
                }

            }
            else -> {}
        }
    }

    companion object{
        private const val LABEL_TEXT_SIZE = 24
        private const val LABEL_ZOOM_LEVEL = 8
        private const val CURRENT_PLACE = "current_place"
        private const val COLUMN_ID = "id"
        private const val NO_ID = -1
    }

}