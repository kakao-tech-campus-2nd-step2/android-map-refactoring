package campus.tech.kakao.map.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "last_location")
data class Location(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val address: String,
    val x: Double,
    val y: Double
) : Serializable {
    companion object {
        const val LOCATION: String = "LOCATION"
        const val NORMAL: String = "일반"
    }

    fun toHistory(): History {
        return History(
            id = this.id,
            name = this.name
        )
    }
}
