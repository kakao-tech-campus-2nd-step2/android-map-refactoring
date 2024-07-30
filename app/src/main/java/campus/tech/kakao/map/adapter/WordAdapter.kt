package campus.tech.kakao.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.databinding.WordItemBinding
import campus.tech.kakao.map.dto.SearchWord

class WordAdapter(
	private val callback: AdapterCallback
): ListAdapter<SearchWord, WordAdapter.ViewHolder>(
	object : DiffUtil.ItemCallback<SearchWord>(){
		override fun areItemsTheSame(oldItem: SearchWord, newItem: SearchWord): Boolean {
			return oldItem === newItem
		}

		override fun areContentsTheSame(oldItem: SearchWord, newItem: SearchWord): Boolean {
			return oldItem == newItem
		}

	}
) {
	private lateinit var wordBinding: WordItemBinding
	inner class ViewHolder(
		itemView: View
	): RecyclerView.ViewHolder(itemView) {
		init {
			wordBinding.x.setOnClickListener {
				deletedWords(bindingAdapterPosition)
			}
			wordBinding.searchWord.setOnClickListener {
				clickedWord(bindingAdapterPosition)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		wordBinding = WordItemBinding.inflate(inflater, parent, false)
		return ViewHolder(wordBinding.root)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val word = getItem(position)
		wordBinding.word= word.name
	}

	private fun deletedWords(position:Int){
		val word = getItem(position)
		callback.onWordDeleted(word)
	}

	private fun clickedWord(position:Int){
		val word = getItem(position)
		callback.onWordSearched(word)
	}

}