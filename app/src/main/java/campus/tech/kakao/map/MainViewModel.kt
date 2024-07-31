package campus.tech.kakao.map

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kakao.vectormap.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _placeInfo = MutableLiveData<MainPlaceInfo>()
    val placeInfo: LiveData<MainPlaceInfo>
        get() = _placeInfo

    private val _isBottomSheetVisible = MutableLiveData<Boolean>(false)
    val isBottomSheetVisible: LiveData<Boolean>
        get() = _isBottomSheetVisible

    fun setLocation(latitude: Double? = null, longitude: Double? = null): LatLng? {
        return if (latitude != null && longitude != null) {
            LatLng.from(latitude, longitude)
        } else {
            val historyList = preferenceManager.getArrayList(Constants.SEARCH_HISTORY_KEY)
            if (historyList.isNullOrEmpty()) {
                null
            } else {
                val historyLongitude = historyList[0].x?.toDoubleOrNull()
                val historyLatitude = historyList[0].y?.toDoubleOrNull()
                if (historyLongitude != null && historyLatitude != null) {
                    LatLng.from(historyLatitude, historyLongitude)
                } else {
                    null
                }
            }
        }
    }

    fun updatePlaceInfo(name: String?, address: String?) {
        if (!name.isNullOrEmpty() && !address.isNullOrEmpty()) {
            _placeInfo.value = MainPlaceInfo(name, address)
            _isBottomSheetVisible.value = true
        } else {
            _isBottomSheetVisible.value = false
        }
    }
}
