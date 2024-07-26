package campus.tech.kakao.map

import android.app.Application
import campus.tech.kakao.map.di.AppModule
import campus.tech.kakao.map.data.db.DataBase
import campus.tech.kakao.map.repository.KakaoRepository
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit

@HiltAndroidApp
class KyleMaps : Application() {

    var isTestMode: Boolean = false
    lateinit var database: DataBase
    lateinit var retrofit: Retrofit
    lateinit var kakaoRepository: KakaoRepository
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        database = AppModule.provideDatabase(this)
        retrofit = AppModule.provideRetrofit()
        kakaoRepository = AppModule.provideKakaoRepository(retrofit)


    }
}
