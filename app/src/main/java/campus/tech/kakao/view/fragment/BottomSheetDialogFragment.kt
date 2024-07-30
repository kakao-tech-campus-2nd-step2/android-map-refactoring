package campus.tech.kakao.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import campus.tech.kakao.map.databinding.BottomsheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaceInfoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.placeName = it.getString("placeName")
            binding.roadAddressName = it.getString("roadAddressName")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(placeName: String, roadAddressName: String) =
            PlaceInfoBottomSheet().apply {
                arguments = Bundle().apply {
                    putString("placeName", placeName)
                    putString("roadAddressName", roadAddressName)
                }
            }
    }
}