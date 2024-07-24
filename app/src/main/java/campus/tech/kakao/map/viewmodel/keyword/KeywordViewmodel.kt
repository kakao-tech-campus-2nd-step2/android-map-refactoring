package campus.tech.kakao.map.viewmodel.keyword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import campus.tech.kakao.map.repository.keyword.KeywordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class KeywordViewModel @Inject constructor(
    private val repository: KeywordRepository
) : ViewModel() {

    private val _keywords = MutableLiveData<List<String>>()
    val keywords: LiveData<List<String>> get() = _keywords

    init {
        loadKeywords()
    }

    private fun loadKeywords() {
        _keywords.value = repository.read()
    }

    fun saveKeyword(keyword: String) {
        repository.update(keyword)
        loadKeywords() // 업데이트 후 다시 로드하여 UI 업데이트
    }

    fun deleteKeyword(keyword: String) {
        repository.delete(keyword)
        loadKeywords() // 삭제 후 다시 로드하여 UI 업데이트
    }
}
