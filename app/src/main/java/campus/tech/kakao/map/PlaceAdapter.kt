package campus.tech.kakao.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.PlaceItemBinding

class PlaceAdapter(
    private var items: List<Document>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount(): Int = items.size

    fun setData(searchResults: List<Document>) {
        items = searchResults
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Document = items[position]

    inner class PlaceViewHolder(private val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document, position: Int) {
            binding.document = document
            binding.position = position
            binding.clickListener = itemClickListener
            binding.executePendingBindings()
        }
    }
}