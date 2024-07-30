package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.PositionDataSource
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapActivityViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {
    private val _recentPos = MutableLiveData<LatLng>()
    val recentPos : LiveData<LatLng> get() = _recentPos

    init {
        getRecentPos()
    }

    fun getRecentPos(){
        viewModelScope.launch{
            _recentPos.value = placeRepository.fetchLastPosFromDataStore()
        }
    }

    fun setRecentPos(latitude : Double, longitude : Double){
        viewModelScope.launch {
            placeRepository.saveCurrentPosToDataStore(latitude, longitude)
        }
    }
}