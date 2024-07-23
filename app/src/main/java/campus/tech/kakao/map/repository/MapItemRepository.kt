package campus.tech.kakao.map.repository

import campus.tech.kakao.map.database.MapItemDao
import campus.tech.kakao.map.model.MapItemEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapItemRepository @Inject constructor(private val mapItemDao: MapItemDao) {

    suspend fun insert(mapItem: MapItemEntity) {
        mapItemDao.insert(mapItem)
    }

    suspend fun getAllMapItems(): List<MapItemEntity> {
        return mapItemDao.getAllMapItems()
    }

}