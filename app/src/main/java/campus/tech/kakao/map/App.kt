package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.model.datasource.SharedPreferences
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        KakaoMapSdk.init(this, BuildConfig.KAKAO_API_KEY)
        super.onCreate()
    }
}
