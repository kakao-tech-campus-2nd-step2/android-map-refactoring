package campus.tech.kakao.map.ui

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel : ViewModel() {

    private val _searchClickEvent = MutableLiveData<Unit>()
    val searchClickEvent: LiveData<Unit> get() = _searchClickEvent

    fun onSearchClick() {
        _searchClickEvent.value = Unit
    }
}
