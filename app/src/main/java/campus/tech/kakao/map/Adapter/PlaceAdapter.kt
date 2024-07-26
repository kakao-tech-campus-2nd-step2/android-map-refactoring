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

class PlaceAdapter : ListAdapter<Place, PlaceAdapter.PlaceViewHolder>(PlaceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_place_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }


    fun submitList(results: List<SearchResult>?) {

    }


    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val addressTextView: TextView = itemView.findViewById(R.id.place)

        fun bind(place: Place) {
            nameTextView.text = place.place_name
            addressTextView.text = place.address_name
        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem.place_name == newItem.place_name
    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem == newItem
}

