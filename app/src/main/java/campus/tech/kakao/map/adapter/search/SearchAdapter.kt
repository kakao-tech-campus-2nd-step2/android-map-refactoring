package campus.tech.kakao.map.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.model.kakaolocal.LocalUiModel
import campus.tech.kakao.map.viewmodel.OnSearchItemClickListener

class SearchAdapter(
    private val onSearchItemClickListener: OnSearchItemClickListener
) :
    ListAdapter<LocalUiModel, RecyclerView.ViewHolder>(
        object : DiffUtil.ItemCallback<LocalUiModel>() {
            override fun areItemsTheSame(oldItem: LocalUiModel, newItem: LocalUiModel) =
                oldItem.place == newItem.place

            override fun areContentsTheSame(oldItem: LocalUiModel, newItem: LocalUiModel) =
                oldItem == newItem
        }
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchViewHolder) {
            val item = getItem(position)
            holder.bindViewHolder(item, onSearchItemClickListener)
        }
    }

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val itemList: RelativeLayout = view.findViewById(R.id.item_list)
        private val place: TextView = view.findViewById(R.id.tv_place_name)
        private val address: TextView = view.findViewById(R.id.tv_address)
        private val category: TextView = view.findViewById(R.id.tv_category)

        fun bindViewHolder(localUiModel: LocalUiModel, onSearchItemClickListener: OnSearchItemClickListener) {
            place.text = localUiModel.place
            address.text = localUiModel.address
            category.text = localUiModel.category

            itemList.setOnClickListener {
                onSearchItemClickListener.onSearchItemClick(localUiModel)
            }
        }
    }
}
