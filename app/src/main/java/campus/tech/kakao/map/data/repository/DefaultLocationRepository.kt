package campus.tech.kakao.map.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import campus.tech.kakao.map.domain.model.LocationDomain
import campus.tech.kakao.map.di.LocationDataStore
import campus.tech.kakao.map.domain.repository.LocationRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

@ViewModelScoped
class DefaultLocationRepository
@Inject
constructor(
    @LocationDataStore private val dataStore: DataStore<LocationDomain>,
) : LocationRepository {
    override suspend fun saveLocation(location: LocationDomain) {
        dataStore.updateData {
            location
        }
    }

    override suspend fun loadLocation(): LocationDomain {
        return try {
            dataStore.data.firstOrNull() ?: getDefaultLocation()
        } catch (exception: Exception) {
            Log.e("DefaultLocationRepository", "Failed to load location data", exception)
            getDefaultLocation()
        }
    }

    private fun getDefaultLocation(): LocationDomain {
        return LocationDomain(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )
    }
}
