package campus.tech.kakao.map.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import campus.tech.kakao.map.data.history.HistoryDatabase
import campus.tech.kakao.map.data.history.HistoryRepositoryImpl
import campus.tech.kakao.map.data.local_search.LocalSearchService
import campus.tech.kakao.map.data.local_search.SearchLocationRepositoryImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Suppress("UNCHECKED_CAST")
class SearchLocationViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchLocationViewModel::class.java)) {
            val db = Room.databaseBuilder(
                context,
                HistoryDatabase::class.java,
                "history_database"
            ).build()
            val historyRepository = HistoryRepositoryImpl(db.historyDao())

            val localSearchService = Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/v2/local/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LocalSearchService::class.java)
            val searchLocationRepository = SearchLocationRepositoryImpl(localSearchService)

            return SearchLocationViewModel(historyRepository, searchLocationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}