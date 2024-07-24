package campus.tech.kakao.map.Room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import campus.tech.kakao.map.kakaoAPI.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapItemViewModel(context: Context) : ViewModel() {
    val networkService = NetworkService()
    val mapDB = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "kakaoMapDB"
    ).build()

    private val _kakaoMapItemList: MutableLiveData<List<KakaoMapItem>> = MutableLiveData()
    val kakaoMapItemList: LiveData<List<KakaoMapItem>> get() = _kakaoMapItemList

    private val _selectItemList: MutableLiveData<List<MapItem>> = MutableLiveData()
    val selectItemList: LiveData<List<MapItem>> get() = _selectItemList

    init {
        makeAllSelectItemList()
    }

    fun makeAllSelectItemList() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectItemList.postValue(mapDB.selectMapItemDao().getAll())
        }
    }

    suspend fun searchKakaoMapItem(category: String) {
        _kakaoMapItemList.postValue(networkService.searchKakaoMapItem(category))
    }

    fun insertSelectItem(mapItem: MapItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (mapDB.selectMapItemDao().checkItemInDB(mapItem.kakaoId) > 0) {
                mapDB.selectMapItemDao().deleteItem(mapItem.kakaoId)
            }
            mapDB.selectMapItemDao().insertItem(mapItem)
            makeAllSelectItemList()
        }
    }

    fun deleteSelectItem(mapItem: MapItem) {
        viewModelScope.launch(Dispatchers.IO) {
            mapDB.selectMapItemDao().deleteItem(mapItem.kakaoId)
            makeAllSelectItemList()
        }
    }
}