package campus.tech.kakao.model.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import campus.tech.kakao.model.dao.SelectedDataDao
import campus.tech.kakao.model.entity.SelectedData

@Database(entities = [SelectedData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun selectedDataDao(): SelectedDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "selectedData.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}