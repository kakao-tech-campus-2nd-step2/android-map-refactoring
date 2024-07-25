package campus.tech.kakao.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemHistoryBinding

class HistoryAdapter(
    private var historyList: MutableList<Pair<Long, String>>,
    private val itemClickListener: (Long) -> Unit,
    private val itemViewClickListener: (String) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val (id, historyItem) = historyList[position]
        holder.bind(historyItem)

        holder.binding.delButton.setOnClickListener {
            itemClickListener(id)
        }

        holder.binding.root.setOnClickListener {
            itemViewClickListener(historyItem)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    fun updateData(newHistoryList: List<Pair<Long, String>>) {
        historyList.clear()
        historyList.addAll(newHistoryList)
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.historyItem = item
            binding.executePendingBindings()
        }
    }
}