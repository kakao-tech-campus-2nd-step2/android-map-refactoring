package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.databinding.PlaceItemBinding

class DocumentAdapter(
	private val callback: AdapterCallback
): ListAdapter<Document, DocumentAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<Document>(){
		override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
			return oldItem === newItem
		}

		override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
			return oldItem == newItem
		}

	}
) {
	private lateinit var documentBinding: PlaceItemBinding
	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		init {
			itemView.setOnClickListener {
				callback.onPlaceClicked(getItem(bindingAdapterPosition))
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		documentBinding = PlaceItemBinding.inflate(inflater, parent, false)
		return ViewHolder(documentBinding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val document: Document = getItem(position)
		documentBinding.document = document
	}
}


