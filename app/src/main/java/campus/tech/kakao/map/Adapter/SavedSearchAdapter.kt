package campus.tech.kakao.map.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.R

class SavedSearchAdapter(
    private var data: List<String> = listOf(),
    private val onItemClick: (String) -> Unit = {}
) : RecyclerView.Adapter<SavedSearchAdapter.SavedSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_view, parent, false)
        return SavedSearchViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: SavedSearchViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(newData: String) {
        data = listOf(newData)
        notifyDataSetChanged()
    }

    inner class SavedSearchViewHolder(itemView: View, private val onItemClick: (String) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val textView: TextView = itemView.findViewById(R.id.result)
        private val deleteButton: Button = itemView.findViewById(R.id.delete)

        fun bind(searchText: String) {
            textView.text = searchText
            itemView.setOnClickListener {
                onItemClick(searchText)
            }
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val updatedData = data.toMutableList()
                    updatedData.removeAt(position)
                    updateData(updatedData.toString())
                }
            }
        }
    }
}

