package campus.tech.kakao.model

import com.google.gson.annotations.SerializedName

data class ResultSearchKeyword(
    @SerializedName("documents") val documents: List<Place>
)

data class Place(
    @SerializedName("place_name") val placeName: String,
    @SerializedName("road_address_name") val roadAddressName: String?,
    @SerializedName("category_group_code") val categoryName: String?,
    @SerializedName("x") val x : Double?,
    @SerializedName("y") val y : Double?
)