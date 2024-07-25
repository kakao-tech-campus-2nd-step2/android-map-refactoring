package campus.tech.kakao.map.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.data.history.History
import campus.tech.kakao.map.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val searchLocationByHistory: (String) -> Unit,
    private val removeHistory: (String) -> Unit
) : ListAdapter<History, HistoryAdapter.MyViewHolder>(diffUtil) {
    inner class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.locationHistoryNameTextView.setOnClickListener {
                searchLocationByHistory(currentList[bindingAdapterPosition].name)
            }

            binding.removeLocationHistoryButton.setOnClickListener {
                removeHistory(currentList[bindingAdapterPosition].name)
            }
        }

        fun bind(historyData: History) {
            binding.history = historyData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}