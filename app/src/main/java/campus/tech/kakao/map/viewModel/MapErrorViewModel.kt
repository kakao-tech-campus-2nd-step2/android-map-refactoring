package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapErrorViewModel @Inject constructor() : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setErrorMsg(error: String?) {
        if (error != null) {
            _errorMessage.value = error.substringAfterLast(":").trim()
        }
    }
}