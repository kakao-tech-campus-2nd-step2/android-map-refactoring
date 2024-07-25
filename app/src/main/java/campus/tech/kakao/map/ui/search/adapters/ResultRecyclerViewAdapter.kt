package campus.tech.kakao.map.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.databinding.ItemPlaceBinding
import campus.tech.kakao.map.ui.search.interfaces.OnPlaceItemClickListener

class ResultRecyclerViewAdapter(private val clickListener: OnPlaceItemClickListener) :
    ListAdapter<Place, ResultRecyclerViewAdapter.PlaceViewHolder>(PlaceDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int,
    ) {
        val place = getItem(position)
        holder.bind(place)
    }

    class PlaceViewHolder(
        private val binding: ItemPlaceBinding,
        private val clickListener: OnPlaceItemClickListener,
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentPlace: Place

        init {
            itemView.setOnClickListener {
                clickListener.onPlaceItemClicked(currentPlace)
            }
        }

        fun bind(place: Place) {
            currentPlace = place
            binding.placeNameTextView.text = place.name
            binding.placeCategoryTextView.text = place.category
            binding.placeAddressTextView.text = place.address
        }
    }

    private class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(
            oldItem: Place,
            newItem: Place,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Place,
            newItem: Place,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
