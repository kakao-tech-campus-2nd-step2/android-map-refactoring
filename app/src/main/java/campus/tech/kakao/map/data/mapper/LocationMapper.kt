package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.LocationDataProto.LocationData
import campus.tech.kakao.map.domain.model.LocationDomain

fun LocationData.map(): LocationDomain {
    return LocationDomain(
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude,
        address = this.address
    )
}

fun LocationDomain.map(): LocationData {
    return LocationData.newBuilder()
        .setName(this.name)
        .setLatitude(this.latitude)
        .setLongitude(this.longitude)
        .setAddress(this.address)
        .build()
}
