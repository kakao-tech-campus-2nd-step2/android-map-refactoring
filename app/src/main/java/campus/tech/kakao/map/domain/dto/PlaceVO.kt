package campus.tech.kakao.map.domain.dto

import java.io.Serializable

data class PlaceVO(
    val placeName: String,
    val addressName: String,
    val categoryName: String,
    val latitude: Double,
    val longitude: Double,
) : Serializable
