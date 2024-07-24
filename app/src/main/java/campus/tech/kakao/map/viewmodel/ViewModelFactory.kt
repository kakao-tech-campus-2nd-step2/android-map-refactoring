package campus.tech.kakao.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import campus.tech.kakao.map.model.repository.DefaultLocationRepository
import campus.tech.kakao.map.model.repository.DefaultSavedLocationRepository

class ViewModelFactory {
    class LocationViewModelFactory(
        private val locationRepository: DefaultLocationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
                return LocationViewModel(locationRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }

    class SavedLocationViewModelFactory(
        private val savedLocationRepository: DefaultSavedLocationRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(SavedLocationViewModel::class.java)) {
                return SavedLocationViewModel(savedLocationRepository) as T
            } else {
                throw IllegalArgumentException()
            }
        }
    }
}
