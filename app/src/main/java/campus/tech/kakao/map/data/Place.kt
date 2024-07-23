package campus.tech.kakao.map.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place(
    @ColumnInfo val name: String,
    @PrimaryKey val address: String,
    @ColumnInfo val category: String?,
    @ColumnInfo val x: String?,
    @ColumnInfo val y: String?
)
