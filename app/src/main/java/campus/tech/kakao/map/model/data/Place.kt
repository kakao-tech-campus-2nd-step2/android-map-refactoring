package campus.tech.kakao.map.model.data

data class Place(
    val id : Int,
    val name : String,
    val address : String,
    val kind : String,
    val longitude : String, //x longitude 경도
    val latitude : String  //y latitude  위도
)
