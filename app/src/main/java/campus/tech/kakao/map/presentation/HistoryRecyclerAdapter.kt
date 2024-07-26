package campus.tech.kakao.map.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import campus.tech.kakao.map.DatabaseListener
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSearchHistoryBinding
import campus.tech.kakao.map.domain.model.History

class HistoryRecyclerAdapter(
    var searchHistory: List<History>,
    val layoutInflater: LayoutInflater,
    val databaseListener: DatabaseListener
) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {
    inner class HistoryViewHolder(val binding: ItemSearchHistoryBinding) : ViewHolder(binding.root) {
        fun bind(historyItem: History) {
            binding.history = historyItem
        }

        init {
            binding.historyClear.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    databaseListener.deleteHistory(searchHistory[bindingAdapterPosition])
                }
            }

            itemView.setOnClickListener {
                databaseListener.searchHistory(searchHistory[bindingAdapterPosition].name, false)
                databaseListener.insertHistory(searchHistory[bindingAdapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = ItemSearchHistoryBinding.inflate(layoutInflater, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(searchHistory[position])
    }

    override fun getItemCount(): Int {
        return searchHistory.size
    }

    fun refreshList() {
        notifyDataSetChanged()
    }
}