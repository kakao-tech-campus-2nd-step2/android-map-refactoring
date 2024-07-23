package campus.tech.kakao.map.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "history")
data class History(
    @PrimaryKey val id: String,
    val name: String,
) : Serializable
