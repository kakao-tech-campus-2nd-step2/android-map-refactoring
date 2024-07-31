package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.PlaceRepository
import campus.tech.kakao.map.data.PositionDataSource
import campus.tech.kakao.map.utilities.Constants
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapActivityViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {
    private val _recentPos = MutableStateFlow<LatLng>(
        LatLng.from(
            Constants.ChonnamUnivLocation.LATITUDE.toDouble(),
            Constants.ChonnamUnivLocation.LONGITUDE.toDouble()
        )
    )
    val recentPos: StateFlow<LatLng> get() = _recentPos

    fun getRecentPos(){
        viewModelScope.launch{
            _recentPos.value = placeRepository.fetchLastPosFromDataStore()
            Log.d("testtt", "recent" + recentPos.value.toString())

        }
    }

    fun setRecentPos(latitude : Double, longitude : Double){
        viewModelScope.launch {
            placeRepository.saveCurrentPosToDataStore(latitude, longitude)
            getRecentPos()
        }
    }
}