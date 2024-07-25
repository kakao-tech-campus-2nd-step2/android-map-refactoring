package campus.tech.kakao.map.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.model.Place
import campus.tech.kakao.map.di.IoDispatcher
import campus.tech.kakao.map.domain.usecase.GetPlacesByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PlaceViewModel
@Inject
constructor(
    private val getPlacesByCategoryUseCase: GetPlacesByCategoryUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :
    ViewModel() {
    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> get() = _searchResults

    private val _categoryInput = MutableStateFlow<String>("")
    private val _totalPageCount = MutableStateFlow<Int>(0)

    init {
        viewModelScope.launch(ioDispatcher) {
            _categoryInput
                .debounce(300)
                .collectLatest { category ->
                    if (category.isNotBlank()) {
                        searchPlacesByCategory(category, _totalPageCount.value)
                    }
                }
        }
    }

    fun searchPlacesByCategory(
        categoryInput: String,
        totalPageCount: Int,
    ) {
        viewModelScope.launch(ioDispatcher) {
            try {
                val places = getPlacesByCategoryUseCase(categoryInput, totalPageCount)
                _searchResults.emit(places)
            } catch (e: Exception) {
                Log.e("placeViewmodel", "Error searching places by category", e)
            }
        }
    }

    fun updateCategoryInput(categoryInput: String, totalPageCount: Int) {
        viewModelScope.launch {
            _categoryInput.emit(categoryInput)
            _totalPageCount.emit(totalPageCount)
        }
    }
}
