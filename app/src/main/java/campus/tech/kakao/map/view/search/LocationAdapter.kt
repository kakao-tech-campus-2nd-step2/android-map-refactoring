package campus.tech.kakao.map.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemLocationBinding
import campus.tech.kakao.map.view.search.LocationAdapter.LocationViewHolder
import campus.tech.kakao.map.model.Location

class LocationAdapter(
    private val itemSelectedListener: OnItemSelectedListener
) : ListAdapter<Location, LocationViewHolder>(
    object : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationViewHolder { // ViewHolder 생성
        val inflater = LayoutInflater.from(parent.context)
        val itemLocationBinding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(itemLocationBinding, itemSelectedListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) { // bind를 통해 데이터를 연결
        holder.bind(getItem(position))
    }

    class LocationViewHolder( //
        private val itemLocationBinding: ItemLocationBinding,
        private val itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(
        itemLocationBinding.root
    ) {
        fun bind(item: Location) { // ViewHolder와 itemLocationBinding 연동
            itemLocationBinding.location = item

            itemView.setOnClickListener {
                itemSelectedListener.onLocationViewClicked(item)
            }
        }
    }
}