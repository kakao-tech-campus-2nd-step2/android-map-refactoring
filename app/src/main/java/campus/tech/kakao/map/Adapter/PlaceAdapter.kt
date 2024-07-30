package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Data.Place
import campus.tech.kakao.map.Data.SearchResult
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ActivityPlaceItemBinding

class PlaceAdapter : ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ActivityPlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    class PlaceViewHolder(private val binding: ActivityPlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: Place) {
            binding.name.text = place.place_name
            binding.place.text = place.address_name
            binding.category.text = place.category_group_name
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem.place_name == newItem.place_name
    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem == newItem
}


