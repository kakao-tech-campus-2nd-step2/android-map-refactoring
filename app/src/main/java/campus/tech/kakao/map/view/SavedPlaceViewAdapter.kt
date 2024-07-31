package campus.tech.kakao.map.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.SavedPlaceItemBinding
import campus.tech.kakao.map.data.SavedPlace

class SavedPlaceViewAdapter(
    val listener: OnClickSavedPlaceListener
) : ListAdapter<SavedPlace, SavedPlaceViewHolder>(SavedPlaceDiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<SavedPlaceItemBinding>(inflater, R.layout.saved_place_item, parent, false)
        Log.d("testt", "저장된 장소를 띄우는 뷰 홀더 생성")
        return SavedPlaceViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: SavedPlaceViewHolder, position: Int) {
        val currentSavedPlace = getItem(position)
        holder.bind(currentSavedPlace)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun existPlace(savedPlace: SavedPlace) : Boolean = currentList.contains(savedPlace)

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getSavedPlaceAtPosition(position : Int) : SavedPlace{
        return getItem(position)
    }
}

class SavedPlaceDiffCallBack : DiffUtil.ItemCallback<SavedPlace>() {
    override fun areItemsTheSame(oldItem: SavedPlace, newItem: SavedPlace): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SavedPlace, newItem: SavedPlace): Boolean {
        return oldItem.name == newItem.name
    }
}

class SavedPlaceViewHolder(private val binding: SavedPlaceItemBinding, listener: OnClickSavedPlaceListener) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(savedPlace: SavedPlace) {
        binding.savedPlace = savedPlace
    }

}