package campus.tech.kakao.map.utility

import android.app.Application
import campus.tech.kakao.map.BuildConfig
import com.kakao.vectormap.KakaoMapSdk

class MapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }

}

