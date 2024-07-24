package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import campus.tech.kakao.map.dto.MapPosition.getMapPosition
import campus.tech.kakao.map.url.RetrofitData.Companion.getInstance
import campus.tech.kakao.map.dto.Document
import campus.tech.kakao.map.dto.SearchWord
import campus.tech.kakao.map.dto.SearchWordDatabase.Companion.getDatabase
import kotlin.concurrent.thread

class MainViewModel(application: Application): AndroidViewModel(application) {

//	private val wordDbHelper = SearchWordDbHelper(application)
	private val _wordList = MutableLiveData<List<SearchWord>>()
	val wordList: LiveData<List<SearchWord>> get() =  _wordList

	private val retrofitData = getInstance()
	val documentList: LiveData<List<Document>> get() = retrofitData.getDocuments()

	private val _documentClicked = MutableLiveData<Boolean>()
	val documentClicked: LiveData<Boolean> get() = _documentClicked

	private val db = getDatabase(application)

	fun addWord(document: Document){
//		wordDbHelper.addWord(wordFromDocument(document))
		val word = wordFromDocument(document)
		thread {
			deleteWord(word)
			db.searchWordDao().insert(word)
			loadWord()
		}

	}

	private fun wordFromDocument(document: Document): SearchWord {
		return SearchWord(document.placeName, document.addressName, document.categoryGroupName)
	}
	fun deleteWord(word: SearchWord){
		thread {
			db.searchWordDao().delete(word.name, word.address, word.type)
			loadWord()
		}.join()
//		wordDbHelper.deleteWord(id)
	}

	fun loadWord(){
		thread {
			_wordList.postValue(db.searchWordDao().getAll())
		}
	}

	fun searchLocalAPI(query: String){
		retrofitData.searchPlace(query)
	}

	override fun onCleared() {
		super.onCleared()
		db.close()
	}

	private fun setMapInfo(document: Document){
		getMapPosition(getApplication()).setMapInfo(document)
	}

	fun getMapInfo():List<String>{
		val latitude = getMapPosition(getApplication()).getPreferences(LATITUDE,INIT_LATITUDE)
		val longitude = getMapPosition(getApplication()).getPreferences(LONGITUDE,INIT_LONGITUDE)
		val placeName = getMapPosition(getApplication()).getPreferences(PLACE_NAME,"")
		val addressName = getMapPosition(getApplication()).getPreferences(ADDRESS_NAME,"")
		return listOf(latitude, longitude, placeName, addressName)
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

	companion object{
		const val INIT_LATITUDE = "37.402005"
		const val INIT_LONGITUDE = "127.108621"
		const val LATITUDE = "latitude"
		const val LONGITUDE = "longitude"
		const val PLACE_NAME = "placeName"
		const val ADDRESS_NAME = "addressName"
	}
}