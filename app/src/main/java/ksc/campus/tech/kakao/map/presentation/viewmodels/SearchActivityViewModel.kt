package ksc.campus.tech.kakao.map.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.camera.CameraPosition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ksc.campus.tech.kakao.map.BuildConfig
import ksc.campus.tech.kakao.map.domain.models.SearchResult
import ksc.campus.tech.kakao.map.domain.models.LocationInfo
import ksc.campus.tech.kakao.map.domain.repositories.MapViewRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchKeywordRepository
import ksc.campus.tech.kakao.map.domain.repositories.SearchResultRepository
import javax.inject.Inject

@HiltViewModel
class SearchActivityViewModel @Inject constructor(
    private val mapViewRepository: MapViewRepository,
    private val searchResultRepository: SearchResultRepository,
    private val keywordRepository: SearchKeywordRepository
) : ViewModel() {

    private val _searchText: MutableLiveData<String> = MutableLiveData("")
    private val _activeContent: MutableLiveData<ContentType> = MutableLiveData(ContentType.MAP)
    private val _searchResult = MutableStateFlow<List<SearchResult>>(listOf())

    val keywords = keywordRepository.keywords.stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(DEFAULT_TIMEOUT),
    )

    val selectedLocation = mapViewRepository.selectedLocation
    val cameraPosition = mapViewRepository.cameraPosition

    val searchResult: StateFlow<List<SearchResult>> = _searchResult
    val searchText: LiveData<String>
        get() = _searchText
    val activeContent: LiveData<ContentType>
        get() = _activeContent


    init {
        CoroutineScope(Dispatchers.IO).launch {
            mapViewRepository.loadFromSharedPreference()
        }
    }

    private fun search(query: String) {
        searchResultRepository.search(query, BuildConfig.KAKAO_REST_API_KEY)
        switchContent(ContentType.SEARCH_LIST)
        viewModelScope.launch {
            searchResultRepository.searchResult.collectLatest {
                _searchResult.emit(it)
            }
        }
    }

    private fun addKeyword(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            keywordRepository.addKeyword(keyword)
        }
    }

    private fun deleteKeyword(keyword: String) {
        CoroutineScope(Dispatchers.IO).launch {
            keywordRepository.deleteKeyword(keyword)
        }
    }

    private fun updateLocation(address: String, name: String, latitude: Double, longitude: Double) {
        CoroutineScope(Dispatchers.Default).launch {
            mapViewRepository.updateSelectedLocation(
                LocationInfo(address, name, latitude, longitude)
            )
            mapViewRepository.updateCameraPositionWithFixedZoom(latitude, longitude)
        }
    }

    fun clickSearchResultItem(selectedItem: SearchResult) {
        addKeyword(selectedItem.name)
        Log.d("KSC", "lat: ${selectedItem.latitude}, lon: ${selectedItem.longitude}")
        updateLocation(
            selectedItem.address,
            selectedItem.name,
            selectedItem.latitude,
            selectedItem.longitude
        )
        switchContent(ContentType.MAP)
    }

    fun submitQuery(value: String) {
        search(value)
    }

    fun clickKeywordDeleteButton(keyword: String) {
        deleteKeyword(keyword)
    }

    fun clickKeyword(keyword: String) {
        _searchText.postValue(keyword)
        search(keyword)
    }

    fun switchContent(type: ContentType) {
        _activeContent.postValue(type)
    }

    fun updateCameraPosition(position: CameraPosition) {
        CoroutineScope(Dispatchers.Default).launch {
            mapViewRepository.updateCameraPosition(position)
        }
    }

    enum class ContentType { MAP, SEARCH_LIST }

    companion object{
        private const val DEFAULT_TIMEOUT = 5000L
    }
}