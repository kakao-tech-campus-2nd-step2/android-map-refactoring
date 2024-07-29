package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.MapRepositoryInterface
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepositoryInterface
): ViewModel() {
    val placeName = MutableLiveData<String>()
    val placeAddr = MutableLiveData<String>()
    val bottomSheetState = MutableLiveData<Int>()


    fun getLastLocation(): Pair<Double, Double>? {
        return mapRepository.getLastLocation()
    }

    fun setPlaceInfo(placeName: String?, placeAddr: String?){
        if (!placeName.isNullOrEmpty() && !placeAddr.isNullOrEmpty()) {
            this.placeName.value = placeName
            this.placeAddr.value = placeAddr
            bottomSheetState.value = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheetState.value = BottomSheetBehavior.STATE_HIDDEN
        }
    }
    fun extractErrorMsg(fullMsg: String): String {
        val parts = fullMsg.split(": ", limit = 2)
        return if (parts.size > 1) parts[1] else ""
    }

}