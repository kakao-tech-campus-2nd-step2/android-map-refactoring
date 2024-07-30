package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.Data.Place
import campus.tech.kakao.map.Data.SearchResult
import campus.tech.kakao.map.R

class SavedSearchAdapter : RecyclerView.Adapter<SavedSearchAdapter.SavedSearchViewHolder>() {

    private var savedSearches: List<Place> = emptyList()

    class SavedSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
        return SavedSearchViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SavedSearchViewHolder, position: Int) {
        val place = savedSearches[position]
        // Bind place data to the view
    }

    override fun getItemCount(): Int {
        return savedSearches.size
    }

    fun submitList(newList: List<SearchResult>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize() = savedSearches.size
            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return savedSearches[oldItemPosition].place_name == newList[newItemPosition].text
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return savedSearches[oldItemPosition].address_name == newList[newItemPosition].text
            }
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        savedSearches.size
        diffResult.dispatchUpdatesTo(this)
    }
}





