package campus.tech.kakao.map.viewModel

import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val prefersRepo: PreferenceRepository
) : ViewModel() {

    fun saveLocation(locationKey: String, data: String) {
        prefersRepo.setString(locationKey, data)
    }

    fun getLocation(locationKey: String, data: String?): String {
        return prefersRepo.getString(locationKey, data)
    }
}