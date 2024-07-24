package campus.tech.kakao.map.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "selectMapItems")
data class MapItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "x") val x: String,
    @ColumnInfo(name = "y") val y: String,
    @ColumnInfo(name = "kakaoId") val kakaoId: String
)