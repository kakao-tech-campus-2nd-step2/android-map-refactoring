package campus.tech.kakao.map.data.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HISTORY")
data class History(
    @PrimaryKey @ColumnInfo(name = "location_name") val name: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)