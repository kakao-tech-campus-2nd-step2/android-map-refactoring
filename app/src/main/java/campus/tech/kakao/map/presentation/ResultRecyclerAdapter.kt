package campus.tech.kakao.map.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import campus.tech.kakao.map.DatabaseListener
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.ItemSearchResultBinding
import dagger.hilt.android.qualifiers.ApplicationContext

class ResultRecyclerAdapter(
    var searchResult: List<Location>,
    val layoutInflater: LayoutInflater,
    val databaseListener: DatabaseListener
) : RecyclerView.Adapter<ResultRecyclerAdapter.MapViewHolder>() {
    inner class MapViewHolder(val binding: ItemSearchResultBinding) : ViewHolder(binding.root) {

        fun bind(locationItem: Location) {
            binding.location = locationItem
        }

        init {
            itemView.setOnClickListener {
                if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                    val clickedResult = searchResult[bindingAdapterPosition]
                    databaseListener.insertHistory(clickedResult.toHistory())
                    databaseListener.insertLastLocation(clickedResult)
                    databaseListener.showMap()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view = ItemSearchResultBinding.inflate(layoutInflater, parent, false)
        return MapViewHolder(view)
    }

    override fun getItemCount(): Int {
        return searchResult.size
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        holder.bind(searchResult[position])
    }

    fun refreshList() {
        notifyDataSetChanged()
    }
}