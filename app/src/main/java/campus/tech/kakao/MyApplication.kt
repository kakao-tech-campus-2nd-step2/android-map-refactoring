package campus.tech.kakao

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import campus.tech.kakao.map.BuildConfig
import campus.tech.kakao.map.R
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.KAKAO_API_KEY))
    }
}
