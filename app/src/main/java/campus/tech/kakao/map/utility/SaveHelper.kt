package campus.tech.kakao.map.utility

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import campus.tech.kakao.map.R
import org.json.JSONArray

class SaveHelper (private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SavedItems", Context.MODE_PRIVATE)

    fun saveSavedItems(llSave: LinearLayoutCompat) {
        val editor = sharedPreferences.edit()
        val savedNames = JSONArray()
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName != null) {
                savedNames.put(tvSaveName.text.toString())
            }
        }
        editor.putString("savedNames", savedNames.toString())
        editor.apply()
    }

    fun loadSavedItems(llSave: LinearLayoutCompat, hScrollView: HorizontalScrollView, inflater: LayoutInflater, etSearch: EditText) {
        val savedNamesString = sharedPreferences.getString("savedNames", "[]")
        val savedNames = JSONArray(savedNamesString)
        for (i in 0 until savedNames.length()) {
            val name = savedNames.getString(i)
            addSavedItem(name, llSave, hScrollView, inflater, etSearch)
        }
        if (savedNames.length() > 0) {
            hScrollView.visibility = View.VISIBLE
        }
    }

    fun addSavedItem(name: String, llSave: LinearLayoutCompat, hScrollView: HorizontalScrollView, inflater: LayoutInflater, etSearch: EditText) {
        val savedView = inflater.inflate(R.layout.search_save, llSave, false) as ConstraintLayout

        val tvSaveName = savedView.findViewById<TextView>(R.id.tvSaveName)
        val ivDelete = savedView.findViewById<ImageView>(R.id.ivDelete)

        tvSaveName.text = name

        tvSaveName.setOnClickListener {
            etSearch.setText(name)
        }

        ivDelete.setOnClickListener {
            llSave.removeView(savedView)
        }

        llSave.addView(savedView)
        hScrollView.visibility = View.VISIBLE
        scrollToEndOfSearchSave(hScrollView)
    }

    fun removeSavedItem(name: String, llSave: LinearLayoutCompat) {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                llSave.removeViewAt(i)
                break
            }
        }
    }

    fun isProfileInSearchSave(name: String, llSave: LinearLayoutCompat): Boolean {
        for (i in 0 until llSave.childCount) {
            val savedView = llSave.getChildAt(i) as? ConstraintLayout
            val tvSaveName = savedView?.findViewById<TextView>(R.id.tvSaveName)
            if (tvSaveName?.text.toString() == name) {
                return true
            }
        }
        return false
    }

    private fun scrollToEndOfSearchSave(hScrollView: HorizontalScrollView) {
        hScrollView.post {
            hScrollView.fullScroll(View.FOCUS_RIGHT)
        }
    }
}