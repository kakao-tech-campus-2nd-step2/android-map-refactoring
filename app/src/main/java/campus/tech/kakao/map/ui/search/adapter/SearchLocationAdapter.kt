package campus.tech.kakao.map.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.ItemLocationBinding
import campus.tech.kakao.map.data.local_search.Location

class SearchLocationAdapter(
    private val dataList: List<Location>,
    private val addHistory: (String) -> Unit,
    private val addMarker: (Location) -> Unit
) : RecyclerView.Adapter<SearchLocationAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                addHistory(dataList[bindingAdapterPosition].name)
                addMarker(dataList[bindingAdapterPosition])
            }
        }

        fun bind(locationData: Location) {
            binding.location = locationData
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size
}