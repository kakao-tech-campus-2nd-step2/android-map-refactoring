package campus.tech.kakao.map.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.di.IoDispatcher
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain
import campus.tech.kakao.map.domain.usecase.DeleteSearchWordByIdUseCase
import campus.tech.kakao.map.domain.usecase.GetAllSearchWordsUseCase
import campus.tech.kakao.map.domain.usecase.InsertOrUpdateSearchWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSearchWordViewModel
@Inject
constructor(
    private val insertOrUpdateSearchWordUseCase: InsertOrUpdateSearchWordUseCase,
    private val deleteSearchWordByIdUseCase: DeleteSearchWordByIdUseCase,
    private val getAllSearchWordsUseCase: GetAllSearchWordsUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _savedSearchWords = MutableStateFlow<List<SavedSearchWordDomain>>(emptyList())
    val savedSearchWords: StateFlow<List<SavedSearchWordDomain>> get() = _savedSearchWords

    private val _sideEffects = MutableSharedFlow<SideEffect>()
    val sideEffects: SharedFlow<SideEffect> get() = _sideEffects

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        updateSavedSearchWords()
    }

    private fun updateSavedSearchWords() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val searchWords = getAllSearchWordsUseCase()
                _savedSearchWords.emit(searchWords)
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error updating search words", e)
                _errorMessage.emit("검색어 목록 업데이트 중 에러가 발생하였습니다.")
            }
        }
    }

    private fun onPlaceItemClicked(savedSearchWord: SavedSearchWordDomain) {
        viewModelScope.launch(ioDispatcher) {
            try {
                insertOrUpdateSearchWordUseCase(savedSearchWord)
                updateSavedSearchWords()

                _sideEffects.emit(SideEffect.NavigateToMapActivity(savedSearchWord))
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error handling place item click", e)
                _errorMessage.emit("검색어 저장 중 에러가 발생하였습니다.")
            }
        }
    }

    private fun onSavedSearchWordClearImageViewClicked(savedSearchWord: SavedSearchWordDomain) {
        viewModelScope.launch(ioDispatcher) {
            try {
                deleteSearchWordByIdUseCase(savedSearchWord.id)
                updateSavedSearchWords()
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error handling saved search word clear", e)
                _errorMessage.emit("검색어 삭제 중 에러가 발생하였습니다.")
            }
        }
    }

    fun handleUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.OnPlaceItemClicked -> onPlaceItemClicked(event.savedSearchWord)
            is UiEvent.OnSavedSearchWordClearImageViewClicked -> onSavedSearchWordClearImageViewClicked(event.savedSearchWord)
        }
    }

    sealed class UiEvent {
        data class OnPlaceItemClicked(val savedSearchWord: SavedSearchWordDomain) : UiEvent()
        data class OnSavedSearchWordClearImageViewClicked(val savedSearchWord: SavedSearchWordDomain) : UiEvent()
    }
    sealed class SideEffect {
        data class NavigateToMapActivity(val savedSearchWord: SavedSearchWordDomain) : SideEffect()
    }
}
