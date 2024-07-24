package campus.tech.kakao.map.network

import com.google.gson.annotations.SerializedName

data class Document(
//    val id: String,
    @SerializedName("place_name") val name: String,
    @SerializedName("category_group_name") val type: String,
//    val category_group_code: String,
//    val category_group_name: String,
//    val phone: String,
//    val address_name: String,
    @SerializedName("road_address_name") val address: String,
    @SerializedName("x") val longitude: String, // 경도
    @SerializedName("y") val latitude: String,  // 위도
//    val place_url: String,
//    val distance: String
)