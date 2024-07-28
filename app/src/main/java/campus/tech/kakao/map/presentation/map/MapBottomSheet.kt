package campus.tech.kakao.map.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.BottomSheetBinding
import campus.tech.kakao.map.domain.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private var place: Place? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        place = arguments?.getSerializable("place") as? Place
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPlaceName.text = place?.place ?: "알림"
        binding.tvPlaceAddress.text = place?.address ?: "원하는 장소를 검색해 주세요"
    }

}

