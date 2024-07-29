package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedSearchBinding
import campus.tech.kakao.map.model.Place

class SavedSearchAdapter(private val onSearchClicked: (Place) -> Unit, private val onDeleteClicked: (Place) -> Unit) : RecyclerView.Adapter<SavedSearchAdapter.ViewHolder>() {
    private val searches = mutableListOf<Place>()

    class ViewHolder(val binding: ItemSavedSearchBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSavedSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val search = searches[position]
        holder.binding.savedTextView.text = search.name
        holder.binding.savedTextView.setOnClickListener {
            onSearchClicked(search)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClicked(search)
        }
    }

    override fun getItemCount(): Int {
        return searches.size
    }

    fun updateSearches(newSearches: List<Place>) {
        searches.clear()
        searches.addAll(newSearches)
        notifyDataSetChanged()
    }
}
