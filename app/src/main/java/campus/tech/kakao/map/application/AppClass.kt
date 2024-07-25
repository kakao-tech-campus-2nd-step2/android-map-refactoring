package campus.tech.kakao.map.application

import android.app.Application
import campus.tech.kakao.map.R
import campus.tech.kakao.map.database.AppDatabase
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class AppClass : Application() {

    @Inject
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }

    private fun clearDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            database.clearAllTables()
        }
    }

}