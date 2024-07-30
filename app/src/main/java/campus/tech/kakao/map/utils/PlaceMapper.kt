package campus.tech.kakao.map.utils

import campus.tech.kakao.map.data.local.entity.VisitedPlaceEntity
import campus.tech.kakao.map.domain.dto.PlaceVO
import campus.tech.kakao.map.domain.dto.toPlaceVO
import campus.tech.kakao.map.domain.dto.toVisitedPlaceEntity

class PlaceMapper {
    fun mapToEntity(place: PlaceVO) : VisitedPlaceEntity {
        return place.toVisitedPlaceEntity()
    }

    fun mapFromEntity(entity: VisitedPlaceEntity): PlaceVO {
        return entity.toPlaceVO()
    }

}