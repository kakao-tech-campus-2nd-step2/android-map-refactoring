package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.SearchHistoryItemBinding
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.presentation.viewmodel.PlaceViewModel

class SearchHistoryAdapter(
    private val viewModel: PlaceViewModel
) : RecyclerView.Adapter<SearchHistoryAdapter.HistoryViewHolder>() {
    private var historyList: List<String> = emptyList()
    class HistoryViewHolder(private val binding: SearchHistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: PlaceViewModel, history: String) {
            binding.history = history
            binding.viewModel = viewModel
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
        holder.bind(viewModel, historyList[position])
    }

    fun updateData(newHistoryList: List<String>) {
        historyList = newHistoryList
        notifyDataSetChanged()
    }
}
