package campus.tech.kakao.map.presentation.search

import androidx.lifecycle.*
import campus.tech.kakao.map.domain.model.Place
import campus.tech.kakao.map.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: PlaceRepository) : ViewModel() {

    val searchText = MutableLiveData<String>()

    private val _uiState = MutableStateFlow(SearchUiState(true, false))
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _logList = MutableStateFlow<List<Place>>(emptyList())
    val logList: StateFlow<List<Place>> get() = _logList.asStateFlow()

    private val _searchedPlaces = searchText.asFlow()
        .debounce(500L)
        .flatMapLatest { query ->
            if (query.isNotBlank()) {
                flow {
                    val places = getPlaces(query)
                    emit(places)
                }
            } else {
                flowOf(emptyList())
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val searchedPlaces: StateFlow<List<Place>> get() = _searchedPlaces

    init {
        viewModelScope.launch {
            _logList.value = getLogs()
        }
    }

    fun clearSearch() {
        searchText.value = ""
    }

    suspend fun getPlaces(keyword: String): List<Place> {
        return repository.getPlaces(keyword)
    }

    suspend fun getPlaceById(id: String): Place? {
        return repository.getPlaceById(id)
    }

    private suspend fun getLogs(): List<Place> {
        return repository.getLogs()
    }

    fun updateLogs(place: Place) {
        viewModelScope.launch {
            val updatedList = _logList.value.toMutableList()
            val existingLog = updatedList.find { it.id == place.id }
            if (existingLog != null) {
                updatedList.remove(existingLog)
                updatedList.add(0, existingLog)
            } else {
                updatedList.add(0, place)
            }
            _logList.value = updatedList
            repository.updateLogs(updatedList)
        }
    }

    fun removeLog(id: String) {
        viewModelScope.launch {
            repository.removeLog(id)
            _logList.value = getLogs()
        }
    }
}
