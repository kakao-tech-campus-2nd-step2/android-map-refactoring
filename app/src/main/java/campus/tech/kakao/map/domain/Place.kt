package campus.tech.kakao.map.domain

import campus.tech.kakao.map.data.PlaceEntity

data class Place(
    val name: String,
    val address: String,
    val category: String?,
    val x: String?,
    val y: String?
) {
    fun toEntity(): PlaceEntity {
        return PlaceEntity(name = name, address = address, category = category, x = x, y = y)
    }
}