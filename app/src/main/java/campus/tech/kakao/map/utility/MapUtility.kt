package campus.tech.kakao.map.utility

import android.graphics.Color
import campus.tech.kakao.map.R
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

object MapUtility {
    fun camera(kakaoMap: KakaoMap, latitude: Double, longitude: Double) {
        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))
        kakaoMap.moveCamera(cameraUpdate)
    }

    fun addMarker(kakaoMap: KakaoMap, latitude: Double, longitude: Double, name: String): LabelOptions? {
        val labelManager = kakaoMap.labelManager
        val iconAndTextStyle = LabelStyles.from(
            LabelStyle.from(R.drawable.location).setTextStyles(25, Color.BLACK)
        )
        val options = LabelOptions.from(LatLng.from(latitude, longitude))
            .setStyles(iconAndTextStyle)
        val layer = labelManager?.layer
        val label = layer?.addLabel(options)
        label?.changeText(name)

        return options
    }
}