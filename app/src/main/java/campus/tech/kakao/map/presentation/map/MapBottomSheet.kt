package campus.tech.kakao.map.presentation.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.domain.model.Place
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MapBottomSheet(private val place: Place?) : BottomSheetDialogFragment() {
    private lateinit var tvPlaceName : TextView
    private lateinit var tvPlaceAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet, container, false)
        tvPlaceName = view.findViewById(R.id.tvPlaceName)
        tvPlaceAddress = view.findViewById(R.id.tvPlaceAddress)
        tvPlaceName.text = place?.place ?: "알림"
        tvPlaceAddress.text = place?.place ?: "원하는 장소를 검색해 주세요"
        return view
    }
}