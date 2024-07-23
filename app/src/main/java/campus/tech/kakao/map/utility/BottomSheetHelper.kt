package campus.tech.kakao.map.utility

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import campus.tech.kakao.map.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class BottomSheetHelper @Inject constructor (private val context: Context){

    fun showBottomSheet(name: String, address: String) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val bottomSheetLayout = LayoutInflater.from(context).inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomSheetLayout)
        val bottomName = bottomSheetLayout.findViewById<TextView>(R.id.tvBName)
        val bottomAddress = bottomSheetLayout.findViewById<TextView>(R.id.tvBAddress)
        bottomName.text= name
        bottomAddress.text = address
        bottomSheetDialog.show()
    }
}