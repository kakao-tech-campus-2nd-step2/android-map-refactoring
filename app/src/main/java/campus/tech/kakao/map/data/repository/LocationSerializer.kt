package campus.tech.kakao.map.data.repository

import androidx.datastore.core.Serializer
import campus.tech.kakao.map.LocationDataProto.LocationData
import campus.tech.kakao.map.data.mapper.LocationMapper
import campus.tech.kakao.map.domain.model.LocationDomain
import java.io.InputStream
import java.io.OutputStream

object LocationSerializer : Serializer<LocationDomain> {
    override val defaultValue: LocationDomain =
        LocationDomain(
            name = "부산대 컴공관",
            latitude = 35.230934,
            longitude = 129.082476,
            address = "부산광역시 금정구 부산대학로 63번길 2",
        )

    override suspend fun readFrom(input: InputStream): LocationDomain {
        return try {
            val locationData = LocationData.parseFrom(input)
            LocationMapper.mapToDomain(locationData)
        } catch (exception: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(
        t: LocationDomain,
        output: OutputStream,
    ) {
        val locationData = LocationMapper.mapToData(t)
        locationData.writeTo(output)
    }
}
