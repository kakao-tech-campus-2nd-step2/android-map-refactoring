package campus.tech.kakao.map.presenter.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import campus.tech.kakao.map.base.ErrorEnum
import campus.tech.kakao.map.presenter.viewModel.MapViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapActivity : AppCompatActivity() {
    private val viewModel: MapViewModel by viewModels()
    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        loadStateFromSharedPreference()
        startMap()
        setSearchListener(makeResultLauncher())
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.currentPlace.observe(this) {
            it?.let { saveStateOnSharedPreference(it.id) }
        }
        binding.mapView.pause()
    }

    private fun loadStateFromSharedPreference() {
        val sharedPreferences = getSharedPreferences(CURRENT_PLACE, Context.MODE_PRIVATE)
        val id = sharedPreferences.getInt(COLUMN_ID, NO_ID)
        if (id != NO_ID) viewModel.initPlace(id)
    }

    private fun startMap() {
        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(error: Exception) {
                val intent = Intent(this@MapActivity, ErrorActivity::class.java)
                intent.putExtra("type", ErrorEnum.MAP_LOAD_ERROR)
                intent.putExtra("msg", error.message)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
        }, object : KakaoMapReadyCallback() {

            override fun onMapReady(kakaoMap: KakaoMap) {
                handleViewOnStateChanged(kakaoMap)
            }
        })
    }

    private fun handleViewOnStateChanged(kakaoMap: KakaoMap) {
        viewModel.currentPlace.observe(this@MapActivity) {
            it?.let {
                val latLng = LatLng.from(it.y, it.x)
                addPinOnMap(kakaoMap, latLng, it.name)
                kakaoMap.moveCamera(
                    CameraUpdateFactory.newCenterPosition(latLng)
                )
            }
        }
    }

    private fun saveStateOnSharedPreference(id: Int) {
        val sharedPreferences = getSharedPreferences(CURRENT_PLACE, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        editor.putInt(COLUMN_ID, id)
        editor.commit()
    }

    private fun addPinOnMap(kakaoMap: KakaoMap, latLng: LatLng, name: String?) {
        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(
                LabelStyle.from(R.drawable.location_pin)
                    .setTextStyles(LABEL_TEXT_SIZE, Color.BLACK)
                    .setZoomLevel(LABEL_ZOOM_LEVEL)
            )
        )
        val options = LabelOptions.from(latLng).setStyles(styles).setTexts(name)
        val layer = kakaoMap.labelManager?.layer

        layer?.addLabel(options)
    }

    private fun setSearchListener(activityResultLauncher: ActivityResultLauncher<Intent>) {
        binding.search.setOnClickListener {
            activityResultLauncher.launch(Intent(this,PlaceSearchActivity::class.java))
        }
    }

    private fun makeResultLauncher() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                result.data?.extras?.getInt(PlaceSearchActivity.INTENT_ID)?.let {
                    viewModel.initPlace(it)
                }

            }

            else -> {}
        }
    }

    companion object {
        private const val LABEL_TEXT_SIZE = 24
        private const val LABEL_ZOOM_LEVEL = 8
        private const val CURRENT_PLACE = "current_place"
        private const val COLUMN_ID = "id"
        private const val NO_ID = -1
    }

}