package campus.tech.kakao.map.model.data

import android.os.Parcel
import android.os.Parcelable

data class Location (
    val name : String = "",
    val address : String = "",
    val latitude : Double = KAKAO_LATITUDE,   //위도
    val longitude : Double = KAKAO_LONGITUDE //경도
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {    //각 필드를 Parcel에 작성
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }

}

val KAKAO_LATITUDE: Double = 37.39571538711179
val KAKAO_LONGITUDE: Double = 127.11051285266876