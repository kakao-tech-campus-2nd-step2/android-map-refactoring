package campus.tech.kakao.Model

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDb(context: Context) {
    private val database: AppDatabase = AppDatabase.getDatabase(context)
    private val selectedDataDao: SelectedDataDao = database.selectedDataDao()

    suspend fun insertIntoSelectedData(name: String): Long {
        val selectedData = SelectedData(name = name)
        return withContext(Dispatchers.IO) {
            selectedDataDao.insert(selectedData)
        }
    }

    suspend fun getAllSelectedData(): List<Pair<Long, String>> {
        return withContext(Dispatchers.IO) {
            selectedDataDao.getAllSelectedData().map { it.id to it.name }
        }
    }

    suspend fun deleteFromSelectedData(id: Long): Int {
        return withContext(Dispatchers.IO) {
            selectedDataDao.deleteById(id)
        }
    }
}