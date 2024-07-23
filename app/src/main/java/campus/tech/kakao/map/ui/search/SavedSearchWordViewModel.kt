package campus.tech.kakao.map.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.map.data.model.SavedSearchWord
import campus.tech.kakao.map.data.repository.SavedSearchWordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedSearchWordViewModel
@Inject
constructor(
    private val savedSearchWordRepository: SavedSearchWordRepository,
) : ViewModel() {
    private val _savedSearchWords = MutableStateFlow<List<SavedSearchWord>>(emptyList())
    val savedSearchWords: StateFlow<List<SavedSearchWord>> get() = _savedSearchWords

    init {
        updateSavedSearchWords()
    }

    fun insertSearchWord(savedSearchWord: SavedSearchWord) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                savedSearchWordRepository.insertOrUpdateSearchWord(savedSearchWord)
                updateSavedSearchWords()
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error inserting search word", e)
            }
        }
    }

    fun deleteSearchWordById(savedSearchWord: SavedSearchWord) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                savedSearchWord.id.let { id ->
                    savedSearchWordRepository.deleteSearchWordById(id)
                    updateSavedSearchWords()
                }
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error deleting search word", e)
            }
        }
    }

    fun updateSavedSearchWords() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val searchWords = savedSearchWordRepository.getAllSearchWords()
                _savedSearchWords.emit(searchWords)
            } catch (e: Exception) {
                Log.e("SavedSearchWordViewModel", "Error updating search words", e)
            }
        }
    }
}
