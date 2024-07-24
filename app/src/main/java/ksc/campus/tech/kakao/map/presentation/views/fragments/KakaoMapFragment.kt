package ksc.campus.tech.kakao.map.presentation.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraPosition
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.R
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.presentation.viewmodels.SearchActivityViewModel
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class KakaoMapFragment @Inject constructor() :
    Fragment() {

    private val viewModel: SearchActivityViewModel by activityViewModels()

    private lateinit var errorTextView: TextView
    private lateinit var retryButton: ImageButton
    private lateinit var errorMessageGroup: Group

    private lateinit var kakaoMapView: MapView
    private var kakaoMap: KakaoMap? = null

    private fun startKakaoMapView() {
        kakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(e: Exception?) {
                showErrorText(getErrorMessage(e?.message ?: ""))
            }

        },
            object : KakaoMapReadyCallback() {
                override fun onMapReady(km: KakaoMap) {
                    kakaoMap = km
                    restorePosition()
                    restoreMarker()
                }
            })
    }

    private fun setKakaoMapMoveListener(){
        kakaoMap?.setOnCameraMoveEndListener { _, position, _ ->
            viewModel.updateCameraPosition(position)
        }
    }

    private fun skipNextCameraMoveListener(){
        kakaoMap?.setOnCameraMoveEndListener { _, _, _ ->
            setKakaoMapMoveListener()
        }
    }

    private fun initiateKakaoMap(view: View) {
        kakaoMapView = view.findViewById(R.id.kakao_map_view)
        startKakaoMapView()
    }

    private fun moveCamera(position: CameraPosition) {
        skipNextCameraMoveListener()
        if (kakaoMap?.isVisible != true) {
            Log.w("KSC", "[moveCamera] mapView not activated")
            return
        }

        val camUpdate =
            CameraUpdateFactory.newCameraPosition(position)
        kakaoMap?.moveCamera(camUpdate)
    }

    private fun initiateViewModelCallbacks() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.cameraPosition.collect{
                    moveCamera(it)
                }
            }

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED){
                viewModel.selectedLocation.collect{
                    updateSelectedLocation(it)
                }
            }
        }
    }

    private fun initiateRetryButton(parent: View) {
        retryButton = parent.findViewById(R.id.retry_button)
        retryButton.setOnClickListener {
            startKakaoMapView()
            setErrorMessageVisibility(false)
        }
    }

    private fun initiateViews(parent: View) {
        errorTextView = parent.findViewById(R.id.text_error)
        errorMessageGroup = parent.findViewById(R.id.error_message_group)

        initiateRetryButton(parent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kakao_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViewModelCallbacks()
        initiateViews(view)
        initiateKakaoMap(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        Log.d("KSC", "on resume")
        kakaoMapView.resume()
        super.onResume()
    }

    override fun onPause() {
        Log.d("KSC", "on pause")
        kakaoMapView.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        kakaoMapView.finish()
        super.onDestroyView()
    }

    private fun restoreMarker() {
        updateSelectedLocation(viewModel.selectedLocation.replayCache.last())
    }

    private fun restorePosition() {
        moveCamera(viewModel.cameraPosition.replayCache.last())
    }

    private fun updateSelectedLocation(locationInfo: LocationInfo?) {
        if (locationInfo == null) {
            clearLabels()
        } else {
            changeSelectedPosition(LatLng.from(locationInfo.latitude, locationInfo.longitude))
        }
    }

    private fun changeSelectedPosition(coordinate: LatLng) {
        clearLabels()
        addLabel(coordinate)
    }

    private fun clearLabels() {
        if (kakaoMap?.isVisible != true) {
            Log.w("KSC", "[clearLabels] mapView not activated")
            return
        }
        val layer = kakaoMap?.labelManager?.layer
        layer?.removeAll()
    }

    private fun addLabel(coordinate: LatLng) {
        if (kakaoMap?.isVisible != true) {
            Log.w("KSC", "[addLabel] mapView not activated")
            return
        }
        val styles = kakaoMap?.labelManager
            ?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.map_pin)))
        val options = LabelOptions.from(coordinate)
        options.setStyles(styles)
        val layer = kakaoMap?.labelManager?.layer
        layer?.addLabel(options)
    }

    private fun getErrorMessage(error: String): String {
        return "지도 인증을 실패 했습니다. \n다시 시도해 주세요.\n\n$error"
    }

    private fun showErrorText(errorMessage: String) {
        setErrorMessageVisibility(true)
        errorTextView.text = errorMessage
    }

    private fun setErrorMessageVisibility(visible: Boolean) {
        errorMessageGroup.isVisible = visible
    }
}