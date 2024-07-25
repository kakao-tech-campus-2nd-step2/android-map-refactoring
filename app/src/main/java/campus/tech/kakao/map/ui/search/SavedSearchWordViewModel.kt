package campus.tech.kakao.map.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.model.SavedSearchWord
import campus.tech.kakao.map.di.IoDispatcher
import campus.tech.kakao.map.domain.usecase.DeleteSearchWordByIdUseCase
import campus.tech.kakao.map.domain.usecase.GetAllSearchWordsUseCase
import campus.tech.kakao.map.domain.usecase.InsertOrUpdateSearchWordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _savedSearchWords = MutableStateFlow<List<SavedSearchWord>>(emptyList())
    val savedSearchWords: StateFlow<List<SavedSearchWord>> get() = _savedSearchWords

    init {
        updateSavedSearchWords()
    }

    fun insertSearchWord(savedSearchWord: SavedSearchWord) {
        viewModelScope.launch(ioDispatcher) {
            try {
                insertOrUpdateSearchWordUseCase(savedSearchWord)
                updateSavedSearchWords()
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error inserting search word", e)
            }
        }
    }

    fun deleteSearchWordById(savedSearchWord: SavedSearchWord) {
        viewModelScope.launch(ioDispatcher) {
            try {
                deleteSearchWordByIdUseCase(savedSearchWord.id)
                updateSavedSearchWords()
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error deleting search word", e)
            }
        }
    }

    private fun updateSavedSearchWords() {
        viewModelScope.launch(ioDispatcher) {
            try {
                val searchWords = getAllSearchWordsUseCase()
                _savedSearchWords.emit(searchWords)
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error updating search words", e)
            }
        }
    }
}
