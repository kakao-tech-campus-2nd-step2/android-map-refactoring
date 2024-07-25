package campus.tech.kakao.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import campus.tech.kakao.view.activity.OnMapErrorActivity
import campus.tech.kakao.view.PlaceInfoBottomSheet
import campus.tech.kakao.viewmodel.MapViewModel
import campus.tech.kakao.viewmodel.MainViewModel
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.FragmentMapBinding
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
class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    var x: Double = 127.108621
    var y: Double = 37.402005
    private var kakaoMap: KakaoMap? = null

    private val mapViewModel: MapViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels({ requireActivity() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lastLocation = mapViewModel.getLastLocation(requireContext())
        y = lastLocation.first
        x = lastLocation.second

        arguments?.let {
            x = it.getDouble("x", x)
            y = it.getDouble("y", y)
            val placeName = it.getString("placeName", "")
            val roadAddressName = it.getString("roadAddressName", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false).apply {
            viewModel = this@MapFragment.mapViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        binding.KakaoMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(p0: Exception) {
                val intent = Intent(requireContext(), OnMapErrorActivity::class.java)
                intent.putExtra("ErrorType", "${p0}")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                requireContext().startActivity(intent)
                requireActivity().finish()
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(readyMap: KakaoMap) {
                kakaoMap = readyMap
                val latLng = LatLng.from(y, x)
                Log.d("latlng", "onMapReady: $y $x")

                val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
                readyMap.moveCamera(cameraUpdate)

                readyMap.setOnCameraMoveEndListener { _, _, _ ->
                    val position = readyMap.cameraPosition!!.position
                    mapViewModel.saveLastLocation(requireContext(), position.latitude, position.longitude)
                }

                readyMap.setOnMapClickListener { _, _, _, _ ->
                    mainViewModel.setSearchFragment()
                }

                arguments?.let {
                    val x = it.getDouble("x", this@MapFragment.x)
                    val y = it.getDouble("y", this@MapFragment.y)
                    val placeName = it.getString("placeName", "")
                    val roadAddressName = it.getString("roadAddressName", "")
                    setCoordinates(x, y, placeName, roadAddressName)
                }
            }
        })

        binding.searchView1.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mainViewModel.setSearchFragment()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.KakaoMapView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.KakaoMapView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setCoordinates(newX: Double, newY: Double, placeName: String, roadAddressName: String) {
        x = newX
        y = newY
        kakaoMap?.let { map ->
            val latLng = LatLng.from(y, x)
            val cameraUpdate = CameraUpdateFactory.newCenterPosition(latLng)
            map.moveCamera(cameraUpdate)
            setMarker(map, x, y)

            PlaceInfoBottomSheet.newInstance(placeName, roadAddressName)
                .show(parentFragmentManager, PlaceInfoBottomSheet::class.java.simpleName)
        }
    }

    private fun setMarker(kakaoMap: KakaoMap, x: Double, y: Double) {
        kakaoMap.labelManager?.clearAll()
        val styles = kakaoMap.labelManager?.addLabelStyles(
            LabelStyles.from(LabelStyle.from(R.drawable.mapmarker))
        )
        val options = LabelOptions.from(LatLng.from(y, x)).setStyles(styles)
        kakaoMap.labelManager?.layer?.addLabel(options)
    }
}