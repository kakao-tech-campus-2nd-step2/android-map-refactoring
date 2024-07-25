package campus.tech.kakao.map.model.data

data class Place(
    val id : Int,
    val name : String,
    val address : String,
    val kind : String,
    val latitude : String, //y latitude  위도
    val longitude : String //x longitude 경도

)

fun Place.toLocation(): Location {
    return Location(
        name = this.name,
        address = this.address,
        latitude = this.latitude.toDouble(),
        longitude = this.longitude.toDouble()

    )
}