package campus.tech.kakao.map.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.data.net.KakaoApiClient
import campus.tech.kakao.map.data.net.KakaoApiClient.api
import campus.tech.kakao.map.util.PlaceContract
import campus.tech.kakao.map.domain.model.Place
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlaceDBHelper(context: Context):
    SQLiteOpenHelper(context, PlaceContract.DATABASE_NAME, null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(PlaceContract.CREATE_QUERY)
        db?.execSQL(PlaceContract.CREATE_LOG_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(PlaceContract.DROP_QUERY)
        db?.execSQL(PlaceContract.DROP_LOG_QUERY)
        onCreate(db)
    }

    suspend fun getPlaces(keyword: String): List<Place> {
        val places = KakaoApiClient.getPlaces(keyword)
        updatePlaces(places)
        return places
    }

    fun updatePlaces(places: List<Place>) {
        val db = writableDatabase

        db.execSQL(PlaceContract.DELETE_QUERY)
        places.forEach {
            val values = ContentValues().apply {
                put(PlaceContract.COLUMN_ID, it.id)
                put(PlaceContract.COLUMN_NAME, it.place)
                put(PlaceContract.COLUMN_LOCATION, it.address)
                put(PlaceContract.COLUMN_TYPE, it.category)
                put(PlaceContract.COLUMN_X_POS, it.xPos)
                put(PlaceContract.COLUMN_Y_POS, it.yPos)
            }
            db.insert(PlaceContract.TABLE_NAME, null, values)
        }
    }

    fun getPlaceById(id: String): Place? {
        val cursor = readableDatabase.query(
            PlaceContract.TABLE_NAME,
            null, "${PlaceContract.COLUMN_ID} = ?", arrayOf(id), null, null, null
        )
        var place: Place? = null
        cursor?.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_NAME))
                val address = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOCATION))
                val type = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_TYPE))
                val xPos = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_X_POS))
                val yPos = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_Y_POS))
                place = Place(id, name, address, type, xPos, yPos)
            }
        }
        return place
    }

    fun updateLogs(logs: List<Place>) {
        val db = writableDatabase
        db.execSQL(PlaceContract.DELETE_LOG_QUERY)
        logs.forEach { placeLog ->
            val values = ContentValues().apply {
                put(PlaceContract.COLUMN_LOG_ID, placeLog.id)
                put(PlaceContract.COLUMN_LOG_NAME, placeLog.place)
            }
            db.insert(PlaceContract.TABLE_LOG_NAME, null, values)
        }
    }

    fun removeLog(id: String) {
        val db = writableDatabase
        db.delete(PlaceContract.TABLE_LOG_NAME, "${PlaceContract.COLUMN_LOG_ID}=?", arrayOf(id))
    }

    fun getLogs(): List<Place> {
        val logs = mutableListOf<Place>()
        val cursor = readableDatabase.query(
            PlaceContract.TABLE_LOG_NAME,
            null, null, null, null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOG_NAME))
                val id = it.getString(it.getColumnIndexOrThrow(PlaceContract.COLUMN_LOG_ID))
                logs.add(Place(id,name, "", "", "",""))
            }
        }
        return logs
    }

    companion object {

        @Volatile
        private var INSTANCE: PlaceDBHelper? = null

        fun getInstance(context: Context): PlaceDBHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PlaceDBHelper(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
