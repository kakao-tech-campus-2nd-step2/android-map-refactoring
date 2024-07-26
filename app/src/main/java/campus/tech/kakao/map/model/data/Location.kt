package campus.tech.kakao.map.model.data

data class Location (
    val name : String,
    val address : String,
    val latitude : Double = KAKAO_LATITUDE,   //위도
    val longitude : Double = KAKAO_LONGITUDE //경도

)

val KAKAO_LATITUDE: Double = 37.39571538711179
val KAKAO_LONGITUDE: Double = 127.11051285266876