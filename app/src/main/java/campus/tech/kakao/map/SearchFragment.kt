package campus.tech.kakao.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import campus.tech.kakao.map.adapter.AdapterCallback
import campus.tech.kakao.map.adapter.DocumentAdapter
import campus.tech.kakao.map.adapter.WordAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(), AdapterCallback {

	private val model: MainViewModel by activityViewModels()
	private lateinit var documentAdapter: DocumentAdapter
	private lateinit var wordAdapter: WordAdapter
	private lateinit var searchBinding: ActivitySearchBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		observeData()
		CoroutineScope(Dispatchers.IO).launch{
			model.loadWord()
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		searchBinding = ActivitySearchBinding.inflate(inflater, container, false)
		setupUI()
		setRecyclerAdapter()
		return searchBinding.root
	}

	private fun observeData(){
		model.documentList.observe(this, Observer {documents ->
			if (documents.isNullOrEmpty()){
				searchBinding.noSearchResult.visibility = View.VISIBLE
				searchBinding.searchResultRecyclerView.visibility = View.GONE
			}else{
				searchBinding.noSearchResult.visibility = View.GONE
				searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
				documentAdapter.submitList(documents)
				searchBinding.searchResultRecyclerView.adapter = documentAdapter
			}
		})
		model.wordList.observe(this, Observer {searchWords ->
			if (searchWords.isNullOrEmpty()){
				searchBinding.searchWordRecyclerView.visibility = View.GONE
			}
			else{
				searchBinding.searchWordRecyclerView.visibility = View.VISIBLE
				wordAdapter.submitList(searchWords)
				searchBinding.searchWordRecyclerView.adapter = wordAdapter
			}
		})
	}
	private fun setRecyclerAdapter(){
		searchBinding.searchResultRecyclerView.layoutManager = LinearLayoutManager(activity)
		searchBinding.searchWordRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
		documentAdapter = DocumentAdapter(this)
		wordAdapter = WordAdapter(this)
	}
	private fun setupUI(){
		searchBinding.searchClear.setOnClickListener {
			searchBinding.search.setText("")
		}
		searchBinding.search.doOnTextChanged { text, _, _, _ ->
			val query = text.toString()
			if (query.isEmpty()){
				searchBinding.noSearchResult.visibility = View.VISIBLE
				searchBinding.searchResultRecyclerView.visibility = View.GONE
			}else{
				searchBinding.noSearchResult.visibility = View.GONE
				searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
				model.searchLocalAPI(query)
			}
			searchBinding.invalidateAll()
		}
	}

	override fun onPlaceClicked(document: Document) {
		model.placeClicked(document)
		parentFragmentManager.popBackStack()
	}

	override fun onWordDeleted(searchWord: SearchWord) {
		CoroutineScope(Dispatchers.IO).launch{
			model.deleteWord(searchWord)
		}
	}

	override fun onWordSearched(searchWord: SearchWord) {
		model.searchLocalAPI(searchWord.name)
	}


}