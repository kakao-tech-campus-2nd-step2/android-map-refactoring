package campus.tech.kakao.map.presenter.View.adapter

import androidx.recyclerview.widget.DiffUtil
import campus.tech.kakao.map.domain.VO.Place

class PlaceDiffUtil : DiffUtil.ItemCallback<Place>(){
    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.name == newItem.name
    }
}