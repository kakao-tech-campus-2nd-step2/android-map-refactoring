package campus.tech.kakao.map.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemSavedSearchWordBinding
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.ui.search.interfaces.OnSavedSearchWordClearImageViewClickListener
import campus.tech.kakao.map.ui.search.interfaces.OnSavedSearchWordTextViewClickListener

class SavedSearchWordRecyclerViewAdapter(
    private val savedSearchWordClearImageViewClickListener: OnSavedSearchWordClearImageViewClickListener,
    private val savedSearchWordTextViewClickListener: OnSavedSearchWordTextViewClickListener,
) :
    ListAdapter<SavedSearchWordDomain, SavedSearchWordRecyclerViewAdapter.SavedSearchWordViewHolder>(
        SavedSearchWordDiffCallback(),
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SavedSearchWordViewHolder {
        val binding =
            ItemSavedSearchWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSearchWordViewHolder(
            binding,
            savedSearchWordClearImageViewClickListener,
            savedSearchWordTextViewClickListener,
        )
    }

    override fun onBindViewHolder(
        holder: SavedSearchWordViewHolder,
        position: Int,
    ) {
        val savedSearchWord = getItem(position)
        holder.bind(savedSearchWord)
    }

    class SavedSearchWordViewHolder(
        private val binding: ItemSavedSearchWordBinding,
        private val savedSearchWordImageViewClickListener: OnSavedSearchWordClearImageViewClickListener,
        private val savedSearchWordTextViewClickListener: OnSavedSearchWordTextViewClickListener,
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var currentSavedSearchWord: SavedSearchWordDomain

        init {
            binding.savedSearchWordClearImageView.setOnClickListener {
                savedSearchWordImageViewClickListener.onSavedSearchWordClearImageViewClicked(
                    currentSavedSearchWord,
                )
            }

            binding.savedSearchWordTextView.setOnClickListener {
                savedSearchWordTextViewClickListener.onSavedSearchWordTextViewClicked(
                    currentSavedSearchWord,
                )
            }
        }

        fun bind(savedSearchWord: SavedSearchWordDomain) {
            currentSavedSearchWord = savedSearchWord
            binding.savedSearchWordTextView.text = savedSearchWord.name
        }
    }
}

private class SavedSearchWordDiffCallback : DiffUtil.ItemCallback<SavedSearchWordDomain>() {
    override fun areItemsTheSame(
        oldItem: SavedSearchWordDomain,
        newItem: SavedSearchWordDomain,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: SavedSearchWordDomain,
        newItem: SavedSearchWordDomain,
    ): Boolean {
        return oldItem == newItem
    }
}
