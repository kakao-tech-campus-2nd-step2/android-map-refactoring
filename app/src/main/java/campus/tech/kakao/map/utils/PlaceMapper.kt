package campus.tech.kakao.map.utils

import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity
import campus.tech.kakao.map.domain.dto.PlaceVO

class PlaceMapper {
    fun mapToEntity(place: PlaceVO) : VisitedPlaceEntity {
        return VisitedPlaceEntity(
            placeName = place.placeName,
            addressName = place.addressName,
            categoryName = place.categoryName,
            latitude = place.latitude,
            longitude = place.longitude
        )
    }

    fun mapFromEntity(entity: VisitedPlaceEntity): PlaceVO {
        return PlaceVO(
            placeName = entity.placeName,
            addressName = entity.addressName,
            categoryName = entity.categoryName,
            latitude = entity.latitude,
            longitude = entity.longitude
        )
    }

}