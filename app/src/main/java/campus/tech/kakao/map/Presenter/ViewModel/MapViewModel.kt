package campus.tech.kakao.map.Presenter.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.Domain.VO.Place
import campus.tech.kakao.map.Domain.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: PlaceRepository
) : ViewModel() {
    private val _currentPlace : MutableLiveData<Place?> = MutableLiveData()
    val currentPlace : LiveData<Place?> = _currentPlace

    fun initPlace(id : Int){
        viewModelScope.launch{
            _currentPlace.value = repository.getFavoriteById(id)
        }
    }

}