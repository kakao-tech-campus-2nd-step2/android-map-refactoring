package campus.tech.kakao.map.model.kakaolocal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocalUiModel(
    val place: String,
    val address: String,
    val category: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable
