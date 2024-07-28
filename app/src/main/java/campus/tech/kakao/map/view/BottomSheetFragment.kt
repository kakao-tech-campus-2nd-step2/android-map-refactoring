package campus.tech.kakao.map.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.BottomSheetBinding
import campus.tech.kakao.map.model.BottomSheetData
class BottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private lateinit var bottomSheetData: BottomSheetData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheetData = bottomSheetData
    }

    companion object {
        fun newInstance(bottomSheetData: BottomSheetData): BottomSheetFragment {
            val fragment = BottomSheetFragment()
            fragment.bottomSheetData = bottomSheetData
            return fragment
        }
    }
}
