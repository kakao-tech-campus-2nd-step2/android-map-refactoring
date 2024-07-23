package campus.tech.kakao.map.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.domain.usecase.GetPlacesByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceViewModel
@Inject
constructor(private val getPlacesByCategoryUseCase: GetPlacesByCategoryUseCase) :
    ViewModel() {
    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> get() = _searchResults

    fun searchPlacesByCategory(
        categoryInput: String,
        totalPageCount: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val places = getPlacesByCategoryUseCase(categoryInput, totalPageCount)
                _searchResults.emit(places)
            } catch (e: Exception) {
                Log.e("placeViewmodel", "Error searching places by category", e)
            }
        }
    }
}
