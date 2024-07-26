package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.PlaceItemBinding
import campus.tech.kakao.map.data.Place

class PlaceViewAdapter(
    val listener: OnClickPlaceListener
) : ListAdapter<Place, PlaceViewHolder>(PlaceDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<PlaceItemBinding>(inflater, R.layout.place_item, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val currentPlace = getItem(position)
        holder.bind(currentPlace, listener)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getPlaceAtPosition(position : Int) : Place{
        return getItem(position)
    }
}

class PlaceDiffCallBack : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}

class PlaceViewHolder(private val binding : PlaceItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(place : Place, listener : OnClickPlaceListener){
        binding.place = place
        binding.listener = listener
    }
}
