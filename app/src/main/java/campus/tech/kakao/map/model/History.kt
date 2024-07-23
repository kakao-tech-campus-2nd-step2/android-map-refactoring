package campus.tech.kakao.map.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HISTORY")
data class History(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "location_name") val name: String
)