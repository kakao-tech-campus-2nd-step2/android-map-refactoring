package campus.tech.kakao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.model.Place
import campus.tech.kakao.view.utils.CategoryGroupCode
import campus.tech.kakao.map.databinding.ItemPlaceBinding

class PlacesAdapter(private var places: List<Place>, private val onItemClick: (Place) -> Unit) : RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder>() {

    private val categoryGroupCode = CategoryGroupCode()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.bind(place)
        holder.itemView.setOnClickListener {
            onItemClick(place)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun updateData(newPlaces: List<Place>) {
        places = newPlaces
        notifyDataSetChanged()
    }

    inner class PlaceViewHolder(private val binding: ItemPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.place = place
            binding.categoryGroupCode = categoryGroupCode
            binding.executePendingBindings()
        }
    }
}