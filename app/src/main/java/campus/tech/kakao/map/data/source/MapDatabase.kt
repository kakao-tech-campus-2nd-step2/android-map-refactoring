package campus.tech.kakao.map.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import campus.tech.kakao.map.domain.model.History
import campus.tech.kakao.map.domain.model.Location
import campus.tech.kakao.map.data.dao.HistoryDao
import campus.tech.kakao.map.data.dao.LastLocationDao

@Database(entities = [Location::class, History::class], version = 1)
abstract class MapDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun lastLocationDao(): LastLocationDao
}