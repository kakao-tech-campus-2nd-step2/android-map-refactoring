package campus.tech.kakao.map

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetManager(
    private val context: Context,
    private val bottomSheet: LinearLayout
) {

    private val sheetBehavior: BottomSheetBehavior<LinearLayout> =
        BottomSheetBehavior.from(bottomSheet)

    init {
        setupBottomSheetCallback()
    }

    private fun setupBottomSheetCallback() {
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 슬라이드 될 때
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> { // 접힘
                        // 상태가 접힘으로 변경될 때 처리할 코드
                    }

                    BottomSheetBehavior.STATE_DRAGGING -> { // 드래그 하는 중
                        // 상태가 드래그 중으로 변경될 때 처리할 코드
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> { // 펼쳐짐
                        // 상태가 펼쳐짐으로 변경될 때 처리할 코드
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> { // 숨겨짐
                        // 상태가 숨겨짐으로 변경될 때 처리할 코드
                    }

                    BottomSheetBehavior.STATE_SETTLING -> { // 안정화되는 중
                        // 상태가 안정화되는 중으로 변경될 때 처리할 코드
                    }
                }
            }
        })
    }

    fun expand() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun collapse() {
        sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun hide() {
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun setBottomSheetText(name: String, address: String) {
        val nameTextView: TextView = bottomSheet.findViewById(R.id.bottom_sheet_name)
        val addressTextView: TextView = bottomSheet.findViewById(R.id.bottom_sheet_address)
        nameTextView.text = name
        addressTextView.text = address
    }
}