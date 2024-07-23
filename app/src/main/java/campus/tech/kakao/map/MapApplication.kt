package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.data.repository.HistoryRepositoryImpl
import campus.tech.kakao.map.data.repository.LastLocationRepositoryImpl
import campus.tech.kakao.map.data.repository.ResultRepositoryImpl
import campus.tech.kakao.map.data.source.MapDbHelper
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MapApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }
}