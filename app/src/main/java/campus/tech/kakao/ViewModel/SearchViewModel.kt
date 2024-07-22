package campus.tech.kakao.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import campus.tech.kakao.Model.RoomDb
import kotlinx.coroutines.launch

class SearchViewModel(private val db: RoomDb) : ViewModel() {
    private val liveSelectedData = MutableLiveData<List<Pair<Long, String>>>()
    val selectedData: MutableLiveData<List<Pair<Long, String>>> get() = liveSelectedData

    fun loadSelectedData() {
        viewModelScope.launch {
            liveSelectedData.value = db.getAllSelectedData()
        }
    }

    fun insertSelectedData(name: String) {
        viewModelScope.launch {
            db.insertIntoSelectedData(name)
            loadSelectedData()
        }
    }

    fun deleteSelectedData(id: Long) {
        viewModelScope.launch {
            db.deleteFromSelectedData(id)
            loadSelectedData()
        }
    }
}