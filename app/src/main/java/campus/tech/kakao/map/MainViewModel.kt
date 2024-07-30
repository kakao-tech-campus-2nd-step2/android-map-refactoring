package campus.tech.kakao.map

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.adapter.DocumentAdapter
import campus.tech.kakao.map.adapter.WordAdapter
import campus.tech.kakao.map.databinding.ActivitySearchBinding
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.MapPositionPreferences
import campus.tech.kakao.map.dto.SearchWord
import campus.tech.kakao.map.dto.SearchWordDao
import campus.tech.kakao.map.url.RetrofitData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	application: Application, private val retrofitData: RetrofitData,
	private val searchWordDao: SearchWordDao, private val mapPosition: MapPositionPreferences
) : AndroidViewModel(application) {

	private val _wordList = MutableLiveData<List<SearchWord>>()
	val wordList: LiveData<List<SearchWord>> get() =  _wordList

//	private val _documentList = MutableLiveData<List<Document>>()
//	val documentList: LiveData<List<Document>> get() = _documentList
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	private val _documentClicked = MutableLiveData<Boolean>()
	val documentClicked: LiveData<Boolean> get() = _documentClicked

	private val _mapInfo = MutableLiveData<List<String>>()
	val mapInfo: LiveData<List<String>> get() = _mapInfo


	fun addWord(document: Document){
		val word = wordFromDocument(document)
		viewModelScope.launch(Dispatchers.IO) {
			deleteWord(word)
			searchWordDao.insert(word)
			loadWord()
		}

	}

	private fun wordFromDocument(document: Document): SearchWord {
		return SearchWord(document.placeName, document.addressName, document.categoryGroupName)
	}
	suspend fun deleteWord(word: SearchWord){
		viewModelScope.launch(Dispatchers.IO) {
			searchWordDao.delete(word.name, word.address, word.type)
			loadWord()
		}.join()
	}

	fun loadWord(){
		viewModelScope.launch(Dispatchers.IO) {
			_wordList.postValue(searchWordDao.getAll())
		}
	}

	fun searchLocalAPI(query: String){
//		viewModelScope.launch(Dispatchers.Main) {
//			retrofitData.searchPlace(query)
//				.collect { documents ->
//					_documentList.value = documents
//					Log.d("testt", documents.toString())
//				}
//
//		}
		retrofitData.searchPlace(query)
	}


	private fun setMapInfo(document: Document){
		mapPosition.setMapInfo(document)
		getMapInfo()
	}

	fun getMapInfo(){
		_mapInfo.value = listOf()
		val latitude = mapPosition.getPreferences(LATITUDE,INIT_LATITUDE)
		val longitude = mapPosition.getPreferences(LONGITUDE,INIT_LONGITUDE)
		val placeName = mapPosition.getPreferences(PLACE_NAME,"")
		val addressName = mapPosition.getPreferences(ADDRESS_NAME,"")
		_mapInfo.value = listOf(latitude, longitude, placeName, addressName)
	}

	fun placeClicked(document: Document){
		_documentClicked.value = false
		addWord(document)
		setMapInfo(document)
		_documentClicked.value = true
	}

	fun documentClickedDone(){
		_documentClicked.value = false
	}

	fun doOnTextChanged(query: String, searchBinding: ActivitySearchBinding){
		if (query.isEmpty()){
			searchBinding.noSearchResult.visibility = View.VISIBLE
			searchBinding.searchResultRecyclerView.visibility = View.GONE
		}else{
			searchBinding.noSearchResult.visibility = View.GONE
			searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
			searchLocalAPI(query)
		}
	}

	fun documentListObserved(documents: List<Document>, searchBinding: ActivitySearchBinding, documentAdapter: DocumentAdapter){
		if (documents.isNullOrEmpty()){
			searchBinding.noSearchResult.visibility = View.VISIBLE
			searchBinding.searchResultRecyclerView.visibility = View.GONE
		}else{
			searchBinding.noSearchResult.visibility = View.GONE
			searchBinding.searchResultRecyclerView.visibility = View.VISIBLE
			documentAdapter.submitList(documents)
			searchBinding.searchResultRecyclerView.adapter = documentAdapter
		}
	}

	fun wordListObserved(searchWords: List<SearchWord>, searchBinding: ActivitySearchBinding, wordAdapter: WordAdapter){
		if (searchWords.isNullOrEmpty()){
			searchBinding.searchWordRecyclerView.visibility = View.GONE
		}
		else{
			searchBinding.searchWordRecyclerView.visibility = View.VISIBLE
			wordAdapter.submitList(searchWords)
			searchBinding.searchWordRecyclerView.adapter = wordAdapter
		}
	}

	companion object{
		const val INIT_LATITUDE = "37.402005"
		const val INIT_LONGITUDE = "127.108621"
		const val LATITUDE = "latitude"
		const val LONGITUDE = "longitude"
		const val PLACE_NAME = "placeName"
		const val ADDRESS_NAME = "addressName"
	}
}