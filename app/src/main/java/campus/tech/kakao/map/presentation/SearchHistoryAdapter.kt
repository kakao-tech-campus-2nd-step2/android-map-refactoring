package campus.tech.kakao.map.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchHistoryItemBinding

class SearchHistoryAdapter(
    private var historyList: MutableList<String>,
    private val onDeleteClick: (String) -> Unit,
    private val onItemClick: (String) -> Unit,
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(private val binding: SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, onDeleteClick: (String) -> Unit, onItemClick: (String) -> Unit) {
            binding.history = item
            binding.clickListener = View.OnClickListener {
                onItemClick(item)
            }
            binding.deleteListener = View.OnClickListener {
                onDeleteClick(item)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HistoryViewHolder {
        val binding = SearchHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
        position: Int,
    ) {
        holder.bind(historyList[position], onDeleteClick, onItemClick)
    }

    fun updateData(newHistoryList: List<String>) {
        historyList.clear()
        historyList.addAll(newHistoryList)
        notifyDataSetChanged()
    }
}
