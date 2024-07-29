package campus.tech.kakao.map.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationDomain(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
) : Parcelable
