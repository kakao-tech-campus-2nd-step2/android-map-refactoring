package campus.tech.kakao.map.data.datasource.Local

import android.database.Cursor
import android.provider.BaseColumns
import campus.tech.kakao.map.domain.vo.Place
import campus.tech.kakao.map.domain.vo.PlaceCategory

object PlaceContract {
    const val DATABASE_NAME = "Place.db"

    object PlaceEntry : BaseColumns {
        const val TABLE_NAME = "place"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_X = "x"
        const val COLUMN_Y = "y"
        private const val NAME_LENGTH = 30
        private const val ADDRESS_LENGTH = 30

        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID int not null," +
                "$COLUMN_NAME varchar($NAME_LENGTH) not null," +
                "$COLUMN_ADDRESS varchar($ADDRESS_LENGTH)," +
                "$COLUMN_CATEGORY int," +
                "$COLUMN_X double," +
                "$COLUMN_Y double);";

        const val SQL_DROP_TABLE = "DROP TABLE if exists $TABLE_NAME"
    }

    object FavoriteEntry : BaseColumns {
        const val TABLE_NAME = "favorite"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_X = "x"
        const val COLUMN_Y = "y"
        private const val NAME_LENGTH = 30
        private const val ADDRESS_LENGTH = 30

        const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
                "$COLUMN_ID int not null," +
                "$COLUMN_NAME varchar($NAME_LENGTH) not null," +
                "$COLUMN_ADDRESS varchar($ADDRESS_LENGTH)," +
                "$COLUMN_CATEGORY int," +
                "$COLUMN_X double," +
                "$COLUMN_Y double);";

        val SQL_DROP_TABLE = "DROP TABLE if exists $TABLE_NAME"

        val SQL_DELETE_ITEM = "DELETE FROM " +
                "$TABLE_NAME " +
                "WHERE " +
                "$COLUMN_NAME = "
    }

    fun getPlaceByCursor(cursor: Cursor): Place? {
        cursor.moveToFirst()
        if(cursor.count == 0) return null
        return Place(
            cursor.getInt(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ID)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_NAME)
            ),
            cursor.getString(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ADDRESS)
            ),
            PlaceCategory.intToCategory(
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_CATEGORY)
                )
            ),
            cursor.getDouble(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_X)
            ),
            cursor.getDouble(
                cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_Y)
            )
        )
    }

    fun getPlaceListByCursor(cursor: Cursor): List<Place> {
        var result = mutableListOf<Place>()

        while (cursor.moveToNext()) {
            val place = Place(
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ID)
                ),
                cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_NAME)
                ),
                cursor.getString(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_ADDRESS)
                ),
                PlaceCategory.intToCategory(
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_CATEGORY)
                    )
                ),
                cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_X)
                ),
                cursor.getDouble(
                    cursor.getColumnIndexOrThrow(PlaceEntry.COLUMN_Y)
                )
            )
            result.add(place)
        }
        return result
    }
}