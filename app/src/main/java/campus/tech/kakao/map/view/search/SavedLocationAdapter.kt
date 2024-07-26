package campus.tech.kakao.map.view.search

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSavedLocationBinding
import campus.tech.kakao.map.view.search.SavedLocationAdapter.SavedLocationViewHolder
import campus.tech.kakao.map.model.SavedLocation

class SavedLocationAdapter(
        private val itemSelectedListener: OnItemSelectedListener
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

    class SavedLocationViewHolder( // getItem을 사용하기 위해 inner로 선언
        private val itemSavedLocationBinding: ItemSavedLocationBinding,
        private val itemSelectedListener: OnItemSelectedListener
    ) : RecyclerView.ViewHolder(itemSavedLocationBinding.root) {
        fun bind(item: SavedLocation) { // ViewHolder와 itemLocationBinding 연동
            itemSavedLocationBinding.savedLocation = item
            itemView.setOnClickListener {
                itemSelectedListener.onSavedLocationViewClicked(item.title)
            }
            itemSavedLocationBinding.savedLocationXButton.setOnClickListener {
                itemSelectedListener.onSavedLocationXButtonClicked(item)
            } // -> item_saved_location.xml에서 data binding을 통해 ImageView에 리스너를 달았는데 작동을 안하네요..! 여기서 리스너를 달아도 괜찮은 걸까요?
        }
    }
}