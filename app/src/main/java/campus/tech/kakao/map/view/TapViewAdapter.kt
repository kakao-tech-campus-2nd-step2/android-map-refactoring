package campus.tech.kakao.map.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.db.entity.Place
import campus.tech.kakao.map.databinding.TabCardBinding

class TapViewAdapter(
    private val onItemRemoved: (Place) -> Unit
) : ListAdapter<Place, TapViewAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Place>(){
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem == newItem
        }

    }
) {

    inner class ViewHolder(private val binding: TabCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.place = place
            binding.executePendingBindings()

            binding.tabCloseButton.setOnClickListener {
                onItemRemoved(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TabCardBinding>(inflater, R.layout.tab_card, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place: Place = getItem(position)
        holder.bind(place)
    }
}