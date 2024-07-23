package campus.tech.kakao.map.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import campus.tech.kakao.map.domain.Place

@Entity(tableName = "places", primaryKeys = ["name", "address"])
data class PlaceEntity(
    @ColumnInfo val name: String,
    @ColumnInfo val address: String,
    @ColumnInfo val category: String?,
    @ColumnInfo val x: String?,
    @ColumnInfo val y: String?
) {
    fun toDomain(): Place {
        return Place(name = name, address = address, category = category, x = x, y = y)
    }
}
