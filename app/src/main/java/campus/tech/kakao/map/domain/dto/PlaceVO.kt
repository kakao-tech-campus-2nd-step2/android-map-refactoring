package campus.tech.kakao.map.domain.dto

import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity
import java.io.Serializable

data class PlaceVO(
    val placeName: String,
    val addressName: String,
    val categoryName: String,
    val latitude: Double,
    val longitude: Double,
) : Serializable


fun PlaceVO.toVisitedPlaceEntity() = VisitedPlaceEntity(
    placeName = placeName,
    addressName = addressName,
    categoryName = categoryName,
    latitude = latitude,
    longitude = longitude
)

fun VisitedPlaceEntity.toPlaceVO() = PlaceVO(
    placeName = this.placeName,
    addressName = this.addressName,
    categoryName = this.categoryName,
    latitude = this.latitude,
    longitude = this.longitude
)
