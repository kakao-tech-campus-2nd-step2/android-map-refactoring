package campus.tech.kakao.map.data.datasource.Local.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import campus.tech.kakao.map.domain.VO.Place
import campus.tech.kakao.map.domain.VO.PlaceCategory
import campus.tech.kakao.map.data.datasource.Local.PlaceContract

@Entity(tableName = PlaceContract.PlaceEntry.TABLE_NAME)
data class PlaceEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = PlaceContract.PlaceEntry.COLUMN_NAME) val name: String,
    @ColumnInfo(name = PlaceContract.PlaceEntry.COLUMN_ADDRESS) var address: String? = null,
    @ColumnInfo(name = PlaceContract.PlaceEntry.COLUMN_CATEGORY) var category: PlaceCategory? = null,
    @ColumnInfo(name = PlaceContract.PlaceEntry.COLUMN_X) var x : Double,
    @ColumnInfo(name = PlaceContract.PlaceEntry.COLUMN_Y) var y : Double,
)

fun PlaceEntity.toVO() : Place {
    return Place(
        this.id,
        this.name,
        this.address,
        this.category,
        this.x,
        this.y
    )
}
