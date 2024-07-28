package campus.tech.kakao.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // 기본값 추가
    val place: String,
    val address: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
)
