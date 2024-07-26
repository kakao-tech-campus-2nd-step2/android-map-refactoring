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
    inner class LocationViewHolder( //
        private val itemLocationBinding: ItemLocationBinding,
        itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(
        itemLocationBinding.root
    ) {
        fun bind(item: Location) { // ViewHolder와 itemLocationBinding 연동
            itemLocationBinding.location = item
        }
        init {
            itemView.setOnClickListener {
                val location = getItem(bindingAdapterPosition)
                itemSelectedListener.onLocationViewClicked(location)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder { // ViewHolder 생성
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(listItemBinding, itemSelectedListener)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) { // bind를 통해 데이터를 연결
        holder.bind(getItem(position))
    }
}