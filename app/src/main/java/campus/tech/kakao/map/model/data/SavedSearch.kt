package campus.tech.kakao.map.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_searches")
data class SavedSearch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0, // Room에서는 autoGenerate를 사용하여 id를 자동으로 생성
    val name: String
)
