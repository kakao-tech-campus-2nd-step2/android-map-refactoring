package campus.tech.kakao.map.Data.Datasource.Local.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import campus.tech.kakao.map.Domain.VO.Place
import campus.tech.kakao.map.Domain.VO.PlaceCategory
import campus.tech.kakao.map.Data.Datasource.Local.PlaceContract

@Entity(tableName = PlaceContract.FavoriteEntry.TABLE_NAME)
data class FavoriteEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = PlaceContract.FavoriteEntry.COLUMN_NAME) val name: String,
    @ColumnInfo(name = PlaceContract.FavoriteEntry.COLUMN_ADDRESS) var address: String? = null,
    @ColumnInfo(name = PlaceContract.FavoriteEntry.COLUMN_CATEGORY) var category: PlaceCategory? = null,
    @ColumnInfo(name = PlaceContract.FavoriteEntry.COLUMN_X) var x : Double,
    @ColumnInfo(name = PlaceContract.FavoriteEntry.COLUMN_Y) var y : Double,
)

fun FavoriteEntity.toVO() : Place {
    return Place(
        this.id,
        this.name,
        this.address,
        this.category,
        this.x,
        this.y
    )
}
