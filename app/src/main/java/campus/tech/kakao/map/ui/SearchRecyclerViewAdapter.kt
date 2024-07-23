package campus.tech.kakao.map.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.Place

class SearchRecyclerViewAdapter(
    private val places: MutableList<Place>,
    private val onItemClick: (Place) -> Unit,
    private val onCloseButtonClick: (Place) -> Unit
) : ListAdapter<Place, SearchRecyclerViewAdapter.SearchViewHolder>(
    object : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }
    }
) {

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.tvSearchName)
        val btnClose: ImageButton = itemView.findViewById(R.id.btnClose)

        init {
            placeName.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onItemClick(place)
            }

            btnClose.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onCloseButtonClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item, parent, false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val place = places[position]
        holder.placeName.text = place.name
    }

    override fun submitList(list: MutableList<Place>?) {
        super.submitList(list)
    }

}