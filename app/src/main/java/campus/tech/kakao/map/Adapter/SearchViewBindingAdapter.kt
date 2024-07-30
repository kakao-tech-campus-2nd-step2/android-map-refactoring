package campus.tech.kakao.map.Adapter

import androidx.appcompat.widget.SearchView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object SearchViewBindingAdapter {

    @BindingAdapter("app:queryText")
    @JvmStatic
    fun setQueryText(view: SearchView, query: String?) {
        if (query != view.query.toString()) {
            view.setQuery(query, false)
        }
    }

    @InverseBindingAdapter(attribute = "app:queryText", event = "queryTextAttrChanged")
    @JvmStatic
    fun getQueryText(view: SearchView): String {
        return view.query.toString()
    }

    @BindingAdapter("queryTextAttrChanged")
    @JvmStatic
    fun setQueryTextListener(view: SearchView, listener: InverseBindingListener) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                listener.onChange()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                listener.onChange()
                return true
            }
        })
    }

    @BindingAdapter("app:onQueryTextSubmit")
    @JvmStatic
    fun setOnQueryTextSubmitListener(view: SearchView, onQueryTextSubmit: (() -> Unit)?) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onQueryTextSubmit?.invoke()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    @BindingAdapter("app:onQueryTextChange")
    @JvmStatic
    fun setOnQueryTextChangeListener(view: SearchView, onQueryTextChange: ((String) -> Unit)?) {
        view.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                onQueryTextChange?.invoke(newText ?: "")
                return true
            }
        })
    }
}

