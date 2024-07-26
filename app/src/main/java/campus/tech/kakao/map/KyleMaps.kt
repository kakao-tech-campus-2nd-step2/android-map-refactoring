package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.di.AppModule
import campus.tech.kakao.map.data.db.DataBase
import com.kakao.vectormap.KakaoMapSdk

class KyleMaps : Application() {

//    var isTestMode: Boolean = false
    lateinit var database: DataBase
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        database = AppModule.provideDatabase(this)
    }
}
