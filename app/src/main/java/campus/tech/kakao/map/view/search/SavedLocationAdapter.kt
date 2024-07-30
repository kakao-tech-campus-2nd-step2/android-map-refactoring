package campus.tech.kakao.map.view.search

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedLocationBinding
import campus.tech.kakao.map.view.search.SavedLocationAdapter.SavedLocationViewHolder
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationAdapter(
        private val itemSelectedListener: ItemSelectedListener
) : ListAdapter<SavedLocation, SavedLocationViewHolder>(
    object: DiffUtil.ItemCallback<SavedLocation>() {
        override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): SavedLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemSavedLocationBinding = ItemSavedLocationBinding.inflate(inflater, parent, false)
        return SavedLocationViewHolder(itemSavedLocationBinding, itemSelectedListener)
    }

    override fun onBindViewHolder(holder:SavedLocationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SavedLocationViewHolder(
        private val itemSavedLocationBinding: ItemSavedLocationBinding,
        private val itemSelectedListener: ItemSelectedListener
    ) : RecyclerView.ViewHolder(itemSavedLocationBinding.root) {
        fun bind(item: SavedLocation) { // ViewHolder와 itemLocationBinding 연동
            itemSavedLocationBinding.savedLocation = item
            itemSavedLocationBinding.onItemSelectedListener = itemSelectedListener
        }
    }
}