package campus.tech.kakao.map.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.PlaceItemBinding
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.presentation.viewmodel.PlaceViewModel

class PlaceAdapter(
    private val viewModel: PlaceViewModel
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    private var places: List<PlaceVO> = emptyList()

    class PlaceViewHolder(private val binding: PlaceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: PlaceViewModel, place: PlaceVO) {
            binding.place = place
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PlaceAdapter.PlaceViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PlaceItemBinding.inflate(layoutInflater, parent, false)
                return PlaceViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaceViewHolder {
        return PlaceViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(
        holder: PlaceViewHolder,
        position: Int,
    ) {
        holder.bind(viewModel, places[position])
    }

    fun updateData(newPlaces: List<PlaceVO>) {
        places = newPlaces
        notifyDataSetChanged()
    }
}
