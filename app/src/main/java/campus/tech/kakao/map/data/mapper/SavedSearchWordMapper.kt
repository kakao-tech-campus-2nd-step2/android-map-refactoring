package campus.tech.kakao.map.data.mapper

import campus.tech.kakao.map.data.model.SavedSearchWord
import campus.tech.kakao.map.domain.model.SavedSearchWordDomain

object SavedSearchWordMapper {
    fun mapToDomain(savedSearchWord: SavedSearchWord): SavedSearchWordDomain {
        return SavedSearchWordDomain(
            id = savedSearchWord.id,
            name = savedSearchWord.name,
            placeId = savedSearchWord.placeId,
            address = savedSearchWord.address,
            longitude = savedSearchWord.longitude,
            latitude = savedSearchWord.latitude
        )
    }

    fun mapToData(savedSearchWordDomain: SavedSearchWordDomain): SavedSearchWord {
        return SavedSearchWord(
            id = savedSearchWordDomain.id,
            name = savedSearchWordDomain.name,
            placeId = savedSearchWordDomain.placeId,
            address = savedSearchWordDomain.address,
            longitude = savedSearchWordDomain.longitude,
            latitude = savedSearchWordDomain.latitude
        )
    }
}
