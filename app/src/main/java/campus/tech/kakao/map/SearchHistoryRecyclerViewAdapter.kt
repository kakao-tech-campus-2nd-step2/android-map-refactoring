package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.db.SearchHistory

class SearchHistoryRecyclerViewAdapter(
    private val searchHistory: MutableList<SearchHistory>,
    private val onItemClick: (SearchHistory) -> Unit,
    private val onItemDelete: (SearchHistory) -> Unit
) : RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = searchHistory[position]
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val searchHistoryBtn: TextView = itemView.findViewById(R.id.search_history_item)
        private val searchHistoryDeleteBtn: ImageView = itemView.findViewById(R.id.search_history_delete_button)

        fun bind(history: SearchHistory) {
            searchHistoryBtn.text = history.placeName
            searchHistoryBtn.setOnClickListener { onItemClick(history) }
            searchHistoryDeleteBtn.setOnClickListener {
                onItemDelete(history)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    searchHistory.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
        }
    }
}
