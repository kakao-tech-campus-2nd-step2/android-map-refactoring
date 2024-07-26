package campus.tech.kakao.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    private val _placeName = MutableLiveData<String>()
    val placeName: LiveData<String> get() = _placeName

    private val _placeAddress = MutableLiveData<String>()
    val placeAddress: LiveData<String> get() = _placeAddress

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _errorVisible = MutableLiveData<Boolean>()
    val errorVisible: LiveData<Boolean> get() = _errorVisible

    private val _bottomSheetVisible = MutableLiveData<Boolean>()
    val bottomSheetVisible: LiveData<Boolean> get() = _bottomSheetVisible

    fun setPlaceName(name: String) {
        _placeName.value = name
    }

    fun setPlaceAddress(name: String) {
        _placeAddress.value = name
    }

    fun setErrorMessage(name: String) {
        _errorMessage.value = name
    }

    fun setErrorVisible(name: Boolean) {
        _errorVisible.value = name
    }

    fun setBottomSheetVisible(name: Boolean) {
        _bottomSheetVisible.value = name
    }
}