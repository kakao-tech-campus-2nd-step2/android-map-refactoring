package campus.tech.kakao.map.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val prefersRepo: PreferenceRepository
) : ViewModel() {

    private val _placeInfoList = MutableLiveData<List<String>>()
    val placeInfoList: LiveData<List<String>> get() = _placeInfoList
    init {
        _placeInfoList.value = listOf("NONE","NONE")
    }

    fun updateInfo(name: String, address: String){
        _placeInfoList.value = listOf(name,address)
    }

    fun saveLocation(locationKey: String, data: String) {
        prefersRepo.setString(locationKey, data)
    }

    fun getLocation(locationKey: String, data: String?): String {
        return prefersRepo.getString(locationKey, data)
    }
}