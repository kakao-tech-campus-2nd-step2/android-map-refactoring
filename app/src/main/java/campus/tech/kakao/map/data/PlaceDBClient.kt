package campus.tech.kakao.map.data

import android.content.Context
import androidx.room.Room

object PlaceDBClient {
    @Volatile
    private var instance: PlaceDatabase? = null

    fun getInstance(context: Context): PlaceDatabase =
        instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context, PlaceDatabase::class.java, "placeDB")
            .build()
}