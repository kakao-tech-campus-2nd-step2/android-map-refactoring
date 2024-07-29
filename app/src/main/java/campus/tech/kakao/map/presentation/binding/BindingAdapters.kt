package campus.tech.kakao.map.presentation.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.presentation.adapter.PlaceAdapter
import campus.tech.kakao.map.presentation.adapter.SearchHistoryAdapter

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("app:items")
    fun setList(recyclerView: RecyclerView, items: List<PlaceVO>?) {
        items?.let {
            (recyclerView.adapter as PlaceAdapter).updateData(it)
        }
    }
    @JvmStatic
    @BindingAdapter("app:histories")
    fun setHistoryList(recyclerView: RecyclerView, items: List<String>?) {
        items?.let {
            (recyclerView.adapter as SearchHistoryAdapter).updateData(it)
        }
    }
}