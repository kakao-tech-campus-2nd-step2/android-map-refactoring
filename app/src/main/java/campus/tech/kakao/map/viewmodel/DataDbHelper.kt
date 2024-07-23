package campus.tech.kakao.map.viewmodel

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import campus.tech.kakao.map.Model.LocationData

class DataDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "LocationDatabase.db"
        private const val TABLE_NAME = "locations"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LOCATION = "location"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_LOCATIONS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_LOCATION + " TEXT," + COLUMN_CATEGORY + " TEXT,"
                + COLUMN_LATITUDE + " REAL," + COLUMN_LONGITUDE + " REAL" + ")")
        db.execSQL(CREATE_LOCATIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertLocation(location: LocationData) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, location.name)
        values.put(COLUMN_LOCATION, location.location)
        values.put(COLUMN_CATEGORY, location.category)
        values.put(COLUMN_LATITUDE, location.latitude)
        values.put(COLUMN_LONGITUDE, location.longitude)
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllLocations(): List<LocationData> {
        val locationList = mutableListOf<LocationData>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        val nameIndex = cursor.getColumnIndex(COLUMN_NAME)
        val locationIndex = cursor.getColumnIndex(COLUMN_LOCATION)
        val categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY)
        val latitudeIndex = cursor.getColumnIndex(COLUMN_LATITUDE)
        val longitudeIndex = cursor.getColumnIndex(COLUMN_LONGITUDE)

        if (cursor.moveToFirst()) {
            do {
                if (nameIndex != -1 && locationIndex != -1 && categoryIndex != -1 && latitudeIndex != -1 && longitudeIndex != -1) {
                    val location = LocationData(
                        cursor.getString(nameIndex),
                        cursor.getString(locationIndex),
                        cursor.getString(categoryIndex),
                        cursor.getDouble(latitudeIndex),
                        cursor.getDouble(longitudeIndex)
                    )
                    locationList.add(location)
                } else {
                    Log.e("DataDbHelper", "Column index not found")
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return locationList
    }


    fun deleteLocation(location: LocationData) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(location.name))
        db.close()
    }

    fun deleteAllLocations() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
}