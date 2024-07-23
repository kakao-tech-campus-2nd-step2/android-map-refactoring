package campus.tech.kakao.map.data.repository

import campus.tech.kakao.map.data.source.MapDatabase
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.domain.repository.LastLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LastLocationRepositoryImpl @Inject constructor(
    database: MapDatabase
) : LastLocationRepository {
    private val lastLocationDao = database.lastLocationDao()

    override suspend fun insertLastLocation(location: Location) {
        withContext(Dispatchers.IO) {
            val prevLastLocation = lastLocationDao.getLastLocation()
            prevLastLocation?.let { lastLocationDao.deleteLastLocation(prevLastLocation) }
            lastLocationDao.insertLastLocation(location)
        }
    }

    override suspend fun clearLastLocation() {
        withContext(Dispatchers.IO) {
            val prevLastLocation = lastLocationDao.getLastLocation()
            prevLastLocation?.let { lastLocationDao.deleteLastLocation(prevLastLocation) }
        }
    }

    override suspend fun getLastLocation(): Location? {
        var res: Location?

        withContext(Dispatchers.IO) {
            res = lastLocationDao.getLastLocation()
        }
        return res
    }
}