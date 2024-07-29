package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.data.model.SavedSearchWordData
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain

fun SavedSearchWordData.map(): SavedSearchWordDomain {
    return SavedSearchWordDomain(
        id = this.id,
        name = this.name,
        placeId = this.placeId,
        address = this.address,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}

fun SavedSearchWordDomain.map(): SavedSearchWordData {
    return SavedSearchWordData(
        id = this.id,
        name = this.name,
        placeId = this.placeId,
        address = this.address,
        longitude = this.longitude,
        latitude = this.latitude,
    )
}
