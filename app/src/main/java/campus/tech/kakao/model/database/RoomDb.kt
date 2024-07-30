package campus.tech.kakao.model.database

import campus.tech.kakao.model.dao.SelectedDataDao
import campus.tech.kakao.model.entity.SelectedData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomDb @Inject constructor(private val selectedDataDao: SelectedDataDao) {

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