package campus.tech.kakao.map.base

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun setVisibleGone(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}