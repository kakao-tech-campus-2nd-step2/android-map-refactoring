package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.domain.model.PlaceDomain
import campus.tech.kakao.map.data.network.dto.PlaceDTO

object PlaceMapper {
    fun mapToDomain(dto: PlaceDTO): PlaceDomain {
        return PlaceDomain(
            id = dto.placeId,
            name = dto.placeName,
            address = dto.address,
            category = dto.category,
            longitude = dto.longitude,
            latitude = dto.latitude,
        )
    }
}
