package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.data.network.dto.PlaceDTO
import campus.tech.kakao.map.domain.model.PlaceDomain

fun PlaceDTO.map(): PlaceDomain {
    return PlaceDomain(
        id = this.placeId,
        name = this.placeName,
        address = this.address,
        category = this.category,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}
