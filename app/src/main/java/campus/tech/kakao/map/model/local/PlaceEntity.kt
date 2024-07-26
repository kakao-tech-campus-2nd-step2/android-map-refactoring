package campus.tech.kakao.map.repository.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "place")
data class PlaceEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "placeName") val placeName: String,
    @ColumnInfo(name = "placeAddress")val placeAddress: String,
    @ColumnInfo(name = "saved") val saved: Boolean = false // search saved 컬럼
)