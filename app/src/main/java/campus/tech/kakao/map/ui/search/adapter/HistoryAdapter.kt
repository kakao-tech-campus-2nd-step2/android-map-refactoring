package campus.tech.kakao.map.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.data.history.History
import campus.tech.kakao.map.databinding.ItemHistoryBinding
import campus.tech.kakao.map.ui.search.SearchLocationViewModel

class HistoryAdapter(
    private val context: Context,
    private val viewModel: SearchLocationViewModel
) : ListAdapter<History, HistoryAdapter.MyViewHolder>(diffUtil) {
    inner class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.locationHistoryNameTextView.setOnClickListener {
                viewModel.searchLocationByHistory(currentList[bindingAdapterPosition].name)
            }

            binding.removeLocationHistoryButton.setOnClickListener {
                viewModel.removeHistory(currentList[bindingAdapterPosition].name)
            }
        }

        fun bind(historyData: History) {
            binding.locationHistoryNameTextView.text = historyData.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(context),
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