package campus.tech.kakao.map.view.search

import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.view.search.SavedLocationAdapter.SavedLocationHolder
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationAdapter(
        private val itemSelectedListener: OnItemSelectedListener
) : ListAdapter<SavedLocation, SavedLocationHolder>(
    object: DiffUtil.ItemCallback<SavedLocation>() {
        override fun areItemsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: SavedLocation, newItem: SavedLocation): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class SavedLocationHolder(
        itemView:View,
        itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(itemView) {
        val savedLocationXButton: ImageView by lazy{
            itemView.findViewById(R.id.savedLocationXButton)
        }
        val savedLocationTextView: TextView by lazy {
            itemView.findViewById(R.id.savedLocationTextView)
        }

        init {
            itemView.setOnClickListener {
                itemSelectedListener.onSavedLocationViewClicked(getItem(bindingAdapterPosition).title)
            }
            savedLocationXButton.setOnClickListener {
                val savedLocation = getItem(bindingAdapterPosition) as SavedLocation
                itemSelectedListener.onSavedLocationXButtonClicked(savedLocation)
            }
        }
    }

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): SavedLocationHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_saved_location, parent, false)
        return SavedLocationHolder(view, itemSelectedListener)
    }

    override fun onBindViewHolder(holder:SavedLocationHolder, position: Int) {
        val savedLocation = getItem(position)
        holder.savedLocationTextView.setText(savedLocation.title)
    }
}