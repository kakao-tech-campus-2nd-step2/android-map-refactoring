package campus.tech.kakao.map.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import campus.tech.kakao.map.domain.model.Place

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val place: String,
    val address: String,
    val type: String,
    val xPos: String,
    val yPos: String
) {
    fun toPlace() = Place(id, place, address, type, xPos, yPos)
}


@Entity(tableName = "logs")
data class PlaceLogEntity(
    @PrimaryKey val id: String,
    val place: String
) {
    fun toPlace() = Place(id, place, "", "", "", "")
}

