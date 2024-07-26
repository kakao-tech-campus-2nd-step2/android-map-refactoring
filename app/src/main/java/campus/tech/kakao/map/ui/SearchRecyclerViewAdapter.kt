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
import campus.tech.kakao.map.databinding.SearchItemBinding
import campus.tech.kakao.map.domain.Place

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

    inner class SearchViewHolder(val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvSearchName.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onItemClick(place)
            }

            binding.btnClose.setOnClickListener {
                val position: Int = bindingAdapterPosition
                val place = places[position]
                onCloseButtonClick(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
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