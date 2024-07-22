package campus.tech.kakao.map

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        KakaoMapSdk.init(this,BuildConfig.KAKAO_API_KEY)
        super.onCreate()
    }
}