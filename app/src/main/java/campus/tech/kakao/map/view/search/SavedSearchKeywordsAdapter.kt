package campus.tech.kakao.map.view.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.SavedSearchKeywordItemBinding
import campus.tech.kakao.map.model.search.SearchKeyword

class SavedSearchKeywordsAdapter(
    private val savedSearchKeywords: List<SearchKeyword>,
    private val layoutInflater: LayoutInflater
) : RecyclerView.Adapter<SavedSearchKeywordsAdapter.ViewHolder>() {

    private lateinit var itemClickListener: OnItemClickListener

    inner class ViewHolder(private val binding: SavedSearchKeywordItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchKeyword) {
            binding.searchKeyword = item

            binding.SavedSearchKeyword.setOnClickListener {
                itemClickListener.onClickSavedSearchKeyword(item)
            }
            binding.delSavedSearchKeyword.setOnClickListener {
                itemClickListener.onClickDelSavedSearchKeyword(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: SavedSearchKeywordItemBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.saved_search_keyword_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedSearchKeywords.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = savedSearchKeywords[position]
        holder.bind(item)
    }

    interface OnItemClickListener {
        fun onClickSavedSearchKeyword(item: SearchKeyword)
        fun onClickDelSavedSearchKeyword(item: SearchKeyword)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
}