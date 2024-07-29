package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R
import campus.tech.kakao.map.data.room.SearchHistoryData
import campus.tech.kakao.map.adapter.listener.RecentAdapterListener
import campus.tech.kakao.map.viewModel.SearchHistoryViewModel
import kotlinx.coroutines.launch

class RecentSearchAdapter(
    private val searchHistoryDataList: List<SearchHistoryData>,
    private val searchHistoryViewModel: SearchHistoryViewModel,
    private val adapterListener: RecentAdapterListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RecentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.recent_search)
        val deleteBtn: ImageButton = view.findViewById(R.id.delete_recent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recent_search_item, parent, false)
        return RecentViewHolder(view)
    }

    override fun getItemCount(): Int = searchHistoryDataList.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = searchHistoryDataList[position]
        val viewHolder = holder as RecentViewHolder
        viewHolder.name.text = item.name

        viewHolder.deleteBtn.setOnClickListener {
            searchHistoryViewModel.viewModelScope.launch {
                searchHistoryViewModel.deleteRecentData(item.name, item.address)
            }
        }

        viewHolder.name.setOnClickListener {
            adapterListener.autoSearch(item.name)
        }
    }
}