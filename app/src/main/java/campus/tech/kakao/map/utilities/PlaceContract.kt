package campus.tech.kakao.map.utilities

import android.provider.BaseColumns

object PlaceContract{
    const val DATABASE_NAME = "PLACE.DB"
    const val VERSION = 1

    object SavedPlaceEntry : BaseColumns{
        const val TABLE_NAME = "SAVED_PLACE"
        const val COLUMN_NAME = "name"
    }
}