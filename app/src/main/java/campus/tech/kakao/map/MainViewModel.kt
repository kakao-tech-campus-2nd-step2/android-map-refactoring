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
    private val context: Context,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _placeName = MutableLiveData<String>("")
    val placeName: LiveData<String>
        get() = _placeName

    private val _placeAddress = MutableLiveData<String>("")
    val placeAddress: LiveData<String>
        get() = _placeAddress

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
                val historyLongitude = historyList[0].document.x.toDouble()
                val historyLatitude = historyList[0].document.y.toDouble()
                LatLng.from(historyLatitude, historyLongitude)
            }
        }
    }

    fun updatePlaceInfo(name: String?, address: String?) {
        _placeName.value = name ?: ""
        _placeAddress.value = address ?: ""
        _isBottomSheetVisible.value = !name.isNullOrEmpty() && !address.isNullOrEmpty()
    }
}
