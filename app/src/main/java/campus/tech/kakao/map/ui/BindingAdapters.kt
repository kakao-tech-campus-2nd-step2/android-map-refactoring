package campus.tech.kakao.map.ui
//
//import android.widget.EditText
//import androidx.databinding.BindingAdapter
//import androidx.databinding.InverseBindingAdapter
//import androidx.databinding.InverseBindingListener
//import androidx.lifecycle.MutableLiveData
//
//@BindingAdapter("app:keyword")
//fun setKeyword(editText: EditText, keyword: MutableLiveData<String>?) {
//    if (keyword != null && editText.text.toString() != keyword.value) {
//        editText.setText(keyword.value)
//    }
//}
//
//@InverseBindingAdapter(attribute = "app:keyword")
//fun getKeyword(editText: EditText): String {
//    return editText.text.toString()
//}
//
//@BindingAdapter("app:keywordAttrChanged")
//fun setKeywordListener(editText: EditText, listener: InverseBindingListener?) {
//    if (listener != null) {
//        editText.addTextChangedListener(object : android.text.TextWatcher {
//            override fun afterTextChanged(s: android.text.Editable?) {
//                listener.onChange()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//        })
//    }
//}