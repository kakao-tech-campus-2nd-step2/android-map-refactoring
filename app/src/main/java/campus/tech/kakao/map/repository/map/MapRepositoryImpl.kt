package campus.tech.kakao.map.repository.map

import android.content.Context
import campus.tech.kakao.map.repository.map.datasource.MapDataSource
import campus.tech.kakao.map.repository.map.datasource.MapDataSourceImpl

class MapRepositoryImpl(private val dataSource: MapDataSource) : MapRepository {
    override fun updateLastPosition(latitude: Double, longitude: Double) {
        dataSource.updateLastPosition(latitude, longitude)
    }

    override fun readLastPosition(): Pair<Double?, Double?> {
        return dataSource.readLastPosition()
    }

    companion object {
        @Volatile
        private var INSTANCE: MapRepositoryImpl? = null

        fun getInstance(context: Context): MapRepository {
            return INSTANCE ?: synchronized(this) {
                val preferences =
                    context.getSharedPreferences("LAST_POSITION", Context.MODE_PRIVATE)
                val dataSource = MapDataSourceImpl(preferences)
                MapRepositoryImpl(dataSource).also { INSTANCE = it }
            }
        }
    }
}
