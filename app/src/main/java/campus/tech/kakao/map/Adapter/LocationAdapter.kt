package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Model.LocationData
import campus.tech.kakao.map.databinding.ItemViewBinding

class LocationAdapter(
    private val onItemClick: (LocationData) -> Unit
) : ListAdapter<LocationData, LocationAdapter.ViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = getItem(position)
        holder.bind(location)
        holder.itemView.setOnClickListener { onItemClick(location) }
    }

    class ViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationData) {
            binding.name.text = location.name
            binding.location.text = location.location
            binding.category.text = location.category
        }
    }

    private class LocationDiffCallback : DiffUtil.ItemCallback<LocationData>() {
        override fun areItemsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: LocationData, newItem: LocationData): Boolean {
            return oldItem == newItem
        }
    }
}