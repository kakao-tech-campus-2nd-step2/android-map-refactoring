package campus.tech.kakao.map.presenter.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.domain.vo.Place
import campus.tech.kakao.map.databinding.PlaceElementBinding

class SearchResultAdapter(
    val onClickAdd: (id: Int) -> Unit
) : ListAdapter<Place, SearchResultAdapter.ViewHolder>(PlaceDiffUtil()) {

    class ViewHolder(private val binding: PlaceElementBinding, onClickAdd: (id : Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var id : Int? = null

        init {
            itemView.setOnClickListener {
                id?.let{ onClickAdd.invoke(it) }
            }
        }

        fun bind(place:Place){
            binding.placeItem = place
            id = place.id
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = PlaceElementBinding.inflate(inflater,parent,false)

        return ViewHolder(view, onClickAdd)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}