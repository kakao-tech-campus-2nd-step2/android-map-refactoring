package campus.tech.kakao.map.presenter.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.domain.vo.Place
import campus.tech.kakao.map.R
import campus.tech.kakao.map.databinding.FavoriteElementBinding

class FavoriteAdapter(
    val onClickDelete: (id : Int) -> Unit
) : ListAdapter<Place, FavoriteAdapter.ViewHolder>(PlaceDiffUtil()) {
    class ViewHolder(private val binding: FavoriteElementBinding, onClickDelete: (id : Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private var curId : Int? = null

        init {
            curId = null
            binding.deleteFavorite.setOnClickListener {
                curId?.let { onClickDelete.invoke(it) }
            }
        }

        fun bind(place: Place){
            binding.favoriteItem = place
            curId = place.id
            binding.executePendingBindings()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = FavoriteElementBinding.inflate(inflater,parent, false)
        return ViewHolder(view, onClickDelete)
    }

}



