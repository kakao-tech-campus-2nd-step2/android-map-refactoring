package campus.tech.kakao.map.ui

import android.widget.Toast
import androidx.lifecycle.*
import campus.tech.kakao.map.data.Profile
import campus.tech.kakao.map.network.Document
import campus.tech.kakao.map.network.KakaoResponse
import campus.tech.kakao.map.network.SearchService
import campus.tech.kakao.map.utility.CategoryGroupCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val searchService: SearchService) : ViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> get() = _profiles

    private val _noResult = MutableLiveData<Boolean>()
    val noResult: LiveData<Boolean> get() = _noResult

    fun searchProfiles(search: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { searchService.searchKeyword(search) }
                handleSearchResult(result)
            } catch (error: Exception) {
                _noResult.value = true
            }
        }

        CategoryGroupCode.categoryMap[search]?.let { categoryCode ->
            viewModelScope.launch {
                try {
                    val result = searchService.searchCategory(categoryCode)
                    handleSearchResult(result)
                } catch (error: Exception) {
                    _noResult.value = true

                }
            }
        }
    }

    private fun handleSearchResult(result: KakaoResponse?) {
        result?.documents?.let { documents ->
            if (documents.isEmpty()) {
                _noResult.value = true
            } else {
                val profilesList = documents.map { it.toProfile() }
                _profiles.value = profilesList
            }
        } ?: run {
            _noResult.value = true
        }
    }

    private fun Document.toProfile(): Profile {
        return Profile(name = this.name, address = this.address, type = this.type, latitude = this.latitude, longitude = this.longitude)
    }
}
