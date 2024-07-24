package campus.tech.kakao.map.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.model.SavedLocation
import campus.tech.kakao.map.model.repository.SavedLocationRepository
import kotlinx.coroutines.launch

class SavedLocationViewModel(
    private val savedLocationRepository: SavedLocationRepository
) : ViewModel() {
    private val _savedLocation = MutableLiveData<MutableList<SavedLocation>>()
    val savedLocation: LiveData<MutableList<SavedLocation>> get() = _savedLocation

    fun setSavedLocation() {
        viewModelScope.launch {
            _savedLocation.value = savedLocationRepository.getSavedLocationAll()
        }
    }
    fun addSavedLocation(id: Long, title: String) {
        val savedLocation = SavedLocation(id, title)
        if (_savedLocation.value?.contains(savedLocation) == false) {
            viewModelScope.launch {
                savedLocationRepository.addSavedLocation(savedLocation)
                val currentList = _savedLocation.value
                if (currentList != null) {
                    currentList.add(savedLocation)
                    _savedLocation.value = currentList
                }
            }
        }
    }

    fun deleteSavedLocation(savedLocation: SavedLocation) {
        viewModelScope.launch {
            savedLocationRepository.deleteSavedLocation(savedLocation)
            val currentList = _savedLocation.value
            if (currentList != null) {
                currentList.remove(savedLocation)
                _savedLocation.value = currentList
            }
        }

    }
}
