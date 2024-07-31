package campus.tech.kakao.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchHistoryItemBinding
class HistoryAdapter(
    var items: List<SearchHistory>,
    var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onXMarkClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = SearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setData(searchHistory: List<SearchHistory>) {
        items = searchHistory
        notifyDataSetChanged()
        Log.d("insert", "리스트에 추가됨!")
    }

    inner class HistoryViewHolder(private val binding: SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchHistory: SearchHistory, position: Int) {
            binding.searchHistory = searchHistory
            binding.position = position
            binding.clickListener = itemClickListener
            binding.executePendingBindings()
        }
    }
}
