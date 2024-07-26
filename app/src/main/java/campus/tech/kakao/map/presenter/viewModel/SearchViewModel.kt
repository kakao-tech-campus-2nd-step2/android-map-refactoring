package campus.tech.kakao.map.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.domain.VO.Place
import campus.tech.kakao.map.domain.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {

    private val _currentResult: MutableLiveData<List<Place>> = MutableLiveData()
    val currentResult: LiveData<List<Place>> = _currentResult
    private val _favoritePlace: MutableLiveData<List<Place>> = MutableLiveData()
    val favoritePlace: LiveData<List<Place>> = _favoritePlace

    init {
        _currentResult.value = listOf<Place>()
        viewModelScope.launch{
            _favoritePlace.value = repository.getCurrentFavorite()
        }
    }

    suspend fun searchPlace(string: String) {
        _currentResult.postValue(repository.getSimilarPlacesByName(string))
    }

    suspend fun searchPlaceRemote(name: String) {
        _currentResult.postValue(repository.searchPlaceRemote(name))
    }

    fun addFavorite(id : Int) {
        val place = findPlaceById(id)

        place?.let {
            viewModelScope.launch {
                repository.addFavorite(it).run {
                    _favoritePlace.value = this
                }
            }
        }
    }

    fun deleteFromFavorite(id : Int) {
        viewModelScope.launch{
            repository.deleteFavorite(id).run {
                _favoritePlace.value = this
            }
        }
    }

    private fun findPlaceById(id:Int) : Place?{
        return currentResult.value?.find{
            it.id == id
        }
    }
}