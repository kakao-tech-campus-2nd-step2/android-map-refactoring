package campus.tech.kakao.map.repository.location

import campus.tech.kakao.map.entity.LocationEntity
import campus.tech.kakao.map.model.Item
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSearcher @Inject constructor(private val itemDao: ItemDao) {

    suspend fun search(keyword: String): List<Item> {
        val locationEntities = itemDao.searchByCategory(keyword)
        return locationEntities.map {
            Item(
                place = it.placeName,
                address = it.addressName,
                category = it.categoryGroupName,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }
    }
}
