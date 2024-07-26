package campus.tech.kakao.map.util

import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("app:isVisible")
    fun setIsVisible(view: android.view.View, isVisible: Boolean) {
        view.isVisible = isVisible
    }
}