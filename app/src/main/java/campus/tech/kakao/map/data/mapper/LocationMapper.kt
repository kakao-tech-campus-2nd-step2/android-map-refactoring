package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.LocationDataProto.LocationData
import campus.tech.kakao.map.domain.model.LocationDomain

object LocationMapper {
    fun mapToDomain(locationData: LocationData): LocationDomain {
        return LocationDomain(
            name = locationData.name,
            latitude = locationData.latitude,
            longitude = locationData.longitude,
            address = locationData.address
        )
    }

    fun mapToData(location: LocationDomain): LocationData {
        return LocationData.newBuilder()
            .setName(location.name)
            .setLatitude(location.latitude)
            .setLongitude(location.longitude)
            .setAddress(location.address)
            .build()
    }
}
