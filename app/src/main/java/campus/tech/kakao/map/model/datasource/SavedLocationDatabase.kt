package campus.tech.kakao.map.model.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.map.model.SavedLocation

@Database(entities = [SavedLocation::class], version = 1)
abstract class SavedLocationDatabase: RoomDatabase() {
    abstract fun savedLocationDao(): SavedLocationDao

    companion object {
        val DATABASE_NAME = "savedLocationDatabase"
    }
}