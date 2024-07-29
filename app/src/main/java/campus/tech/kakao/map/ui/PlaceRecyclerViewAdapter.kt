package campus.tech.kakao.map.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.PlaceItemBinding
import campus.tech.kakao.map.domain.Place

class PlaceRecyclerViewAdapter(
    private val places: MutableList<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<PlaceRecyclerViewAdapter.PlaceViewHolder>() {

    inner class PlaceViewHolder(val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onItemClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.binding.place = place
        holder.binding.executePendingBindings()
    }

    fun updateData(newPlace: List<Place>) {
        places.clear()
        places.addAll(newPlace)
        notifyDataSetChanged()
    }
}
