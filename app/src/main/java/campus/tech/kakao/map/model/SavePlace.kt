package campus.tech.kakao.map.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "savePlace")
data class SavePlace(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val savePlaceName: String
)

