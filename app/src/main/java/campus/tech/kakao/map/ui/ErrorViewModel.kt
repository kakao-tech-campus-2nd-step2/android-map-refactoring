package campus.tech.kakao.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ErrorViewModel: ViewModel() {
    val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}