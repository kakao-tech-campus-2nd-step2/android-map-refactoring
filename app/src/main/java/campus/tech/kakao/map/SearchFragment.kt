package campus.tech.kakao.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import campus.tech.kakao.map.adapter.AdapterCallback
import campus.tech.kakao.map.adapter.DocumentAdapter
import campus.tech.kakao.map.adapter.WordAdapter
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterCallback {

	private val model: MainViewModel by activityViewModels()
	private lateinit var search: EditText
	private lateinit var clear: TextView
	private lateinit var noResult: TextView
	private lateinit var searchResult: RecyclerView
	private lateinit var searchWordResult: RecyclerView
	private lateinit var documentAdapter: DocumentAdapter
	private lateinit var wordAdapter: WordAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		model.documentList.observe(this, Observer {documents ->
			if (documents.isNullOrEmpty()){
				noResult.visibility = View.VISIBLE
				searchResult.visibility = View.GONE
			}else{
				noResult.visibility = View.GONE
				searchResult.visibility = View.VISIBLE
				documentAdapter.submitList(documents)
				searchResult.adapter = documentAdapter
			}
		})
		model.loadWord()
		model.wordList.observe(this, Observer {searchWords ->
			if (searchWords.isNullOrEmpty()){
				searchWordResult.visibility = View.GONE
			}
			else{
				searchWordResult.visibility = View.VISIBLE
				wordAdapter.submitList(searchWords)
				searchWordResult.adapter = wordAdapter
			}
		})
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		val view = inflater.inflate(R.layout.activity_search, container, false)
		setupUI(view)
		searchResult.layoutManager = LinearLayoutManager(activity)
		searchWordResult.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
		documentAdapter = DocumentAdapter(this)
		wordAdapter = WordAdapter(this)
		search.doOnTextChanged { text, _, _, _ ->
			val query = text.toString()
			if (query.isEmpty()){
				noResult.visibility = View.VISIBLE
				searchResult.visibility = View.GONE
			}else{
				noResult.visibility = View.GONE
				searchResult.visibility = View.VISIBLE
				model.searchLocalAPI(query)
			}
		}
		return view
	}
	private fun setupUI(view: View){
		search = view.findViewById(R.id.search)
		clear = view.findViewById(R.id.search_clear)
		noResult = view.findViewById(R.id.no_search_result)
		searchResult = view.findViewById(R.id.search_result_recycler_view)
		searchWordResult = view.findViewById(R.id.search_word_recycler_view)
		clear.setOnClickListener {
			search.setText("")
		}
	}

	override fun onPlaceClicked(document: Document) {
		model.placeClicked(document)
		parentFragmentManager.popBackStack()
	}

	override fun onWordDeleted(searchWord: SearchWord) {
		model.deleteWord(searchWord)
	}

	override fun onWordSearched(searchWord: SearchWord) {
		model.searchLocalAPI(searchWord.name)
	}


}