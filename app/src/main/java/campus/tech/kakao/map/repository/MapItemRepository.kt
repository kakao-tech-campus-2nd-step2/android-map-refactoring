package campus.tech.kakao.map.repository

import campus.tech.kakao.map.database.MapItemDao
import campus.tech.kakao.map.model.MapItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapItemRepository @Inject constructor(private val mapItemDao: MapItemDao) {

    suspend fun insert(mapItem: MapItemEntity) {
        mapItemDao.insert(mapItem)
    }

    suspend fun insertAll(mapItems: List<MapItemEntity>) {
        mapItemDao.insertAll(mapItems)
    }

    suspend fun getAllMapItems(): List<MapItemEntity> {
        return mapItemDao.getAllMapItems()
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            mapItemDao.deleteAll()
        }
    }

}